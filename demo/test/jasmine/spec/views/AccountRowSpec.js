define(['models/Account',
    'views/AccountRow',
    'sinon',
    'jasmine-jquery'], function (Account, AccountRow, sinon) {

    describe('Load AccountRow View', function () {

        describe('Given AccountRow view', function () {
            var account, accountRow, spyOnToggle, spyOnRender;

            beforeEach(function () {
                // create spies
                spyOnToggle = sinon.spy(AccountRow.prototype, 'toggleSelection');
                spyOnRender = sinon.spy(AccountRow.prototype, 'render');

                // create objects
                account = new Account({
                    name: 'OneTwoThree',
                    number: '123',
                    selected: true
                });

                accountRow = new AccountRow({
                    model: account
                });

                // render view
                $('#sandbox').html(accountRow.render().el);
            });

            afterEach(function () {
                accountRow.remove();
                spyOnToggle.restore();
                spyOnRender.restore();
            });

            describe('when initialize and render with selected account', function () {
                it('has three cells with correct details', function () {
                    var $el = $('#sandbox tr td'),
                    numberCell = $el[0],
                    nameCell = $el[1],
                    selectCell = $el[2];

                    expect($el.length).toBe(3);
                    expect($(numberCell).text()).toEqual(account.get('number'));
                    expect($(nameCell).text()).toEqual(account.get('name'));
                    expect($(selectCell).find('i')).toHaveClass('icon-ok');
                });
            });

            describe('when updateSelection with false', function () {
                it('should show that account is not selected', function () {
                    accountRow.updateSelection(false);
                    var $icon = $('#sandbox tr td:nth-child(3) i');
                    expect($icon).not.toHaveClass('icon-ok');
                });
            });

            describe('when updateSelection with true', function () {
                it('should show that account is selected', function () {
                    accountRow.updateSelection(true);
                    var $icon = $('#sandbox tr td:nth-child(3) i');
                    expect($icon).toHaveClass('icon-ok');
                });
            });

            describe('when toggle selection', function () {
                it('should show that account is not selected', function () {
                    accountRow.toggleSelection();
                    var $icon = $('#sandbox tr td:nth-child(3) i');
                    expect($icon).not.toHaveClass('icon-ok');
                });

                it('should show that account is selected if toggled again', function () {
                    accountRow.toggleSelection();
                    accountRow.toggleSelection();
                    var $icon = $('#sandbox tr td:nth-child(3) i');
                    expect($icon).toHaveClass('icon-ok');
                });
            });

            describe('when click on the row', function () {
                it('should show that account is not selected', function () {
                    $('#sandbox tr').click();

                    var $icon = $('#sandbox tr td:nth-child(3) i');
                    expect($icon).not.toHaveClass('icon-ok');
                });

                it('should show that account is selected if clicked again', function () {
                    $('#sandbox tr').click().click();

                    var $icon = $('#sandbox tr td:nth-child(3) i');
                    expect($icon).toHaveClass('icon-ok');
                });
            });

            // another way using mock
            describe('when click on the row (using spy)', function () {
                it('should toggleSelection', function () {
                    $('#sandbox tr').click();
                    expect(spyOnToggle.callCount).toBe(1);
                    // expect(spyOnToggle).toHaveBeenCalledOnce();
                });

                it('should toggleSelection twice on two clicks', function () {
                    $('#sandbox tr').click().click();
                    expect(spyOnToggle.callCount).toBe(2);
                    // expect(spyOnToggle).toHaveBeenCalledTwice();
                });
            });

            describe('when model changes', function () {
                it('should render', function () {
                    spyOnRender.reset();
                    account.set('name', 'new name');
                    expect(spyOnRender.callCount).toBe(1);
                });
            });

        });
    });
});
