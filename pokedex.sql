sqlite3 pokedex.db 

CREATE TABLE pokemon(
	pokemon_id integer(750) KEY NOT NULL; 
	pokemon_name char(15) TEXT UNIQUE NOT NULL;
);

CREATE INDEX pokemon_name ON pokemon (pokemon_name);

INSERT INTO "pokemon" VALUES (1, "Bulbasaur"); 
INSERT Into "pokemon" VALUES (2, "Ivysaur"); 

