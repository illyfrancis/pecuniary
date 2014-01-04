mongoexport --db openbbh --collection transactions --out ./data/transactions.export
mongoexport --db openbbh --collection accounts --out ./data/accounts.export
<<<<<<< HEAD
mongoexport --db openbbh --collection reportschema --out ./data/reportschema.export
mongoexport --db openbbh --collection securities --out ./data/securities.export

=======
mongoexport --db openbbh --collection preferences --out ./data/preferences.export
mongoexport --db openbbh --collection country --out ./data/country.export
mongoexport --db openbbh --collection reportschema --out ./data/reportschema.export
mongoexport --db openbbh --collection securities --out ./data/securities.export
>>>>>>> 2677b0d75b0959762136978f5b15485e5a5d33cf
