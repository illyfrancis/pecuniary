define(['collections/SimpleAccounts', 'models/Account'], function (Accounts, Account) {
    describe('Given Accounts Collection', function () {
        var accts;

        it('fetch accounts for perf checks (must be run in order)', function () {
            var accounts = new Accounts();
            accounts.fetch();

            waitsFor(function () {
                return accounts.length > 0;
            }, 'accounts never fetched', 10000);

            runs(function () {
                // run perf check
                accts = accounts.pluck('number');
            });
        });

        it('check size', function () {
            expect(accts.length).toEqual(2000);
        });

        xit('concat accts', function () {
            accts = accts.concat(accts);
            // expect(accts.length).toEqual(4000);
            // accts = accts.concat(accts);
            // expect(accts.length).toEqual(8000);
            accts = accts.concat(accts);
            expect(accts.length).toEqual(16000);
        });

        it('search for a number', function () {
            var index = _.indexOf(accts, '6023717');
            expect(index).toEqual(1999);

            index = _.lastIndexOf(accts, '6023717');
            expect(index).toEqual(accts.length-1);
        });

        it('search for non-existing number', function () {
            var index = _.indexOf(accts, '9xxxxxx');
            expect(index).toEqual(-1);
        });

        it('delete an item from array', function () {
            var length = accts.length;
            var index = _.indexOf(accts, '6023717');
            var deleted = accts.splice(index, 1);
            expect(deleted[0]).toEqual('6023717');
            expect(accts.length).toEqual(length-1);
        });

        it('add an item to array', function () {
            var length = accts.length;
            accts.push('6015085');
            expect(accts.length).toEqual(length+1);

            var index = _.lastIndexOf(accts, '6015085');
            expect(index).toEqual(length);
        });

        it('fetch more with argument', function () {
            var Search = Backbone.Model.extend({
                limit: 5,
                skip: 1,
                searchUrl: '/api/accounts/search?limit={{limit}}&skip={{skip}}',
                urlRoot: function () {
                    return this.searchUrl.replace('{{limit}}', this.limit).replace('{{skip}}', this.skip);
                }
            });

            var q = new Search();
            q.set('s', accts.join());
            q.save();



/*            Backbone.sync('create', q, {
                data: {
                    s: accts.join()
                },
                success: function () {
                    console.log('done');
                },
                error: function () {
                    alert('error');
                }
            });
*/
/*            var accounts = new Accounts();
            accounts.fetch({
                success: function () {
                    console.log('done');
                    var dummy = new Accounts();
                    dummy.url = dummy.url + "/search";
                    accounts.sync('create', dummy, {
                        data: {
                            s: accts.join()
                        }
                    })
                },
                error: function () {
                    alert('error');
                }
            });
*/        });
    });
});
