package CSE_111_Project;

import java.util.Scanner;
import java.sql.*;

public class Pokedex {
	// Global Variables
	static int distinct_id_gen = 11;
	static Connection conn;
	static Statement stmt;
	static Scanner in;
	
	/**
	 * Main class
	 * @param args
	 */
	public static void main (String args[]) {
		try {
			// Set up stuff
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:/Users/Hunter/workspace/CSE_111_Project/Pokedex.db");
			stmt = conn.createStatement();
			stmt.setQueryTimeout(30);
			System.out.println("Database Connection Established");
			in = new Scanner(System.in);
			
			System.out.println("Welcome to the Pokedex!");
			
			// Loop now that everything is set up
			while (true) {
				System.out.println("Press 1 for Pokemon lookup, 2 for owned Pokemon lookup, 3 for editing data, 4 to exit");
				int i = in.nextInt();
				in.nextLine();
				
				// Switch statement
				switch (i) {
					case 1:
						DoPokemonLookup();
						break;
					case 2:
						DoOwnedLookup();
						break;
					case 3:
						EditData();
						break;
					case 4:
						conn.close();
						System.exit(0);
						break;
					default:
						continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Look up Pokemon in the Database.
	 */
	public static void DoPokemonLookup() {
		try {
			System.out.println("What is the name of the Pokemon to look up?");
			String name = in.nextLine();
			stmt = null;
			stmt = conn.createStatement();
			
			String q1 = "SELECT * FROM Pokemon WHERE name = '" + name + "';";
			boolean cor = stmt.execute(q1);
			
			if (cor == false) {
				System.out.println("Incorrect Pokemon name.");
				return;
			}
			
			ResultSet r1 = stmt.executeQuery(q1);
			r1.next();
			int pokedex_id = r1.getInt("pokedex_id");
			
			System.out.println("Pokemon Name: " + r1.getString("name"));
			System.out.println("Pokedex ID Number: " + pokedex_id);
			System.out.println("Pokemon Primary Type: " + r1.getString("type_one"));
			System.out.println("Pokemon Secondary Type: " + r1.getString("type_two"));
			System.out.println("Pokemon Generation: " + r1.getString("generation"));
			
			String q3 = "SELECT height, weight, hatch_time, egg_group_one, egg_group_two FROM Static_Attributes s, Pokemon p WHERE p.pokedex_id = " + pokedex_id + " AND p.pokedex_id = s.pokedex_id;";
			ResultSet r3 = stmt.executeQuery(q3);
			r3.next();
			
			System.out.println("Static Attributes");
			System.out.println("Pokemon Height: " + r3.getString(1));
			System.out.println("Pokemon Weight: " + r3.getString(2));
			System.out.println("Number of Steps to Hatch: " + r3.getString(3));
			System.out.println("Egg Group One: " + r3.getString(4));
			System.out.println("Egg Group Two: " + r3.getString(5));
			
			String q4 = "SELECT * FROM Evolutions e, Pokemon p WHERE p.pokedex_id = " + pokedex_id + " AND p.pokedex_id = e.pokedex_id;";
			ResultSet r4 = stmt.executeQuery(q4);
			r4.next();
			
			System.out.println("Evolutionary Family");
			System.out.println("Stage One: " + r4.getString("stage_one"));
			System.out.println("Stage Two: " + r4.getString("stage_two"));
			System.out.println("Stage Three: " + r4.getString("stage_three"));
			System.out.println("Megaevolution: " + r4.getString("megaevolution"));
			
			String q2 = "SELECT ability_name, flavor_text FROM Pokemon p, Ability a, Pokemon_Abilities pa WHERE p.pokedex_id = " + pokedex_id +" AND p.pokedex_id = pa.pokedex_id AND pa.ability_id = a.ability_id;";
			ResultSet r2 = stmt.executeQuery(q2);
			
			System.out.println("Possible Abilities");
			while (r2.next()) {
				System.out.println(r2.getString("ability_name") + ": " + r2.getString("flavor_text"));
			}
			
			/*
			System.out.println("Do you want to look up another Pokemon (yes/no): ");
			String ans = in.nextLine();
			if (ans.equals("yes")) {
				DoPokemonLookup();
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Look up Owned Pokemon
	 */
	public static void DoOwnedLookup() {
		try {
			System.out.println("Look up owned Pokemon by name.");
		
			stmt = null; 
			stmt = conn.createStatement();
			
			System.out.println("Enter Pokemon Name: ");
			String name = in.nextLine();
		/*
			System.out.println("Enter Character ID: ");
			int id = in.nextInt();
			in.nextLine();
		*/
			String q1 = "SELECT * FROM Owned_Pokemon op, Pokemon p WHERE p.name = '" + name + "' AND p.pokedex_id = op.pokedex_id;";
			ResultSet r1 = stmt.executeQuery(q1);
			r1.next();
			int distinct_id = r1.getInt("distinct_id");
			int ability_id = r1.getInt("ability_id");
			int pokedex_id = r1.getInt("pokedex_id");
			
			System.out.println("Pokemon Name: " + r1.getString("name"));
			System.out.println("Pokedex ID: " + r1.getInt("pokedex_id"));
			System.out.println("Distinct ID: " + distinct_id);
			System.out.println("Pokemon Gender: " + r1.getString("gender"));
			System.out.println("Pokemon Level: " + r1.getInt("level"));
			System.out.println("Type One: " + r1.getString("type_one"));
			System.out.println("Type Two: " + r1.getString("type_two"));
			
			String q2 = "SELECT ability_name, flavor_text FROM Ability WHERE ability_id = " + ability_id + ";";
			ResultSet r2 = stmt.executeQuery(q2);
			r2.next();
			
			System.out.println("Pokemon Ability");
			System.out.println(r2.getString("ability_name") + ": " + r2.getString("flavor_text"));
			
			String q3 = "SELECT * FROM Static_Attributes s, Pokemon p WHERE p.pokedex_id = " + pokedex_id + " AND p.pokedex_id = s.pokedex_id;";
			ResultSet r3 = stmt.executeQuery(q3);
			r3.next();
			
			System.out.println("Static Attributes");
			System.out.println("Pokemon Height: " + r3.getString("height"));
			System.out.println("Pokemon Weight: " + r3.getString("weight"));
			System.out.println("Number of Steps to Hatch: " + r3.getString("hatch_time"));
			System.out.println("Egg Group One: " + r3.getString("egg_group_one"));
			System.out.println("Egg Group Two: " + r3.getString("egg_group_two"));
			
			String q4 = "SELECT * FROM Evolutions e, Pokemon p WHERE p.pokedex_id = " + pokedex_id + " AND p.pokedex_id = e.pokedex_id;";
			ResultSet r4 = stmt.executeQuery(q4);
			r4.next();
			
			System.out.println("Evolutionary Family");
			System.out.println("Stage One: " + r4.getString("stage_one"));
			System.out.println("Stage Two: " + r4.getString("stage_two"));
			System.out.println("Stage Three: " + r4.getString("stage_three"));
			System.out.println("Megaevolution: " + r4.getString("megaevolution"));
			
			String q5 = "SELECT * FROM Owned_Stats s, Owned_Pokemon o WHERE o.distinct_id = " + distinct_id + " AND s.distinct_id = o.distinct_id;";
			ResultSet r5 = stmt.executeQuery(q5);
			r5.next();
			
			System.out.println("Stats");
			System.out.println("HP: " + r5.getInt("hp"));
			System.out.println("Speed: " + r5.getInt("speed"));
			System.out.println("Attack: " + r5.getInt("attack"));
			System.out.println("Defense: " + r5.getInt("defense"));
			System.out.println("Special Attack: " + r5.getInt("special_attack"));
			System.out.println("Special Defense: " + r5.getInt("special_defense"));
			
			String q6 = "SELECT * FROM Caught_Location c, Owned_Pokemon o WHERE o.distinct_id = " + distinct_id + " AND c.distinct_id = o.distinct_id;";
			ResultSet r6 = stmt.executeQuery(q6);
			r6.next();
			
			System.out.println("Caught Location: " + r6.getString("location"));
			System.out.println("Caught Region: " + r6.getString("region"));
			
			String q7 = "SELECT * FROM Character c, Owned_pokemon o, Trainer_Pokemon t WHERE t.distinct_id = " + distinct_id + " AND t.distinct_id = o.distinct_id AND t.trainer_id = c.trainer_id;";
			ResultSet r7 = stmt.executeQuery(q7);
			r7.next();
			
			System.out.println("Character Stats");
			System.out.println("Character ID: " + r7.getInt("trainer_id"));
			System.out.println("Region: " + r7.getString("region"));
			System.out.println("Generation: " + r7.getString("generation"));
			System.out.println("Number of Pokemon Owned: " + r7.getInt("number_pokemon_owned"));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void EditData() {
		System.out.println("What would you like to do? Press 1 to Edit Data, 2 to Edit Character, 3 to Edit Caught Location, 4 to Edit Stats, 5 to insert a new Pokemon, and 6 to delete a Pokemon");
		int sw = in.nextInt();
		in.nextLine();
		switch (sw) {
			case 1: 
				EditData_1();
				break;
			case 2:
				EditData_2();
				break;
			case 3:
				EditData_3();
				break;
			case 4:
				EditData_4();
				break;
			case 5:
				EditData_5();
				break;
			case 6:
				EditData_6();
				break;
			default:
				break;
		}
	}
	
	public static void EditData_1() {
		
		System.out.println("Edit Data on owned Pokemon");
		//Scanner input = new Scanner(System.in);
		
		String gender_g;
		int level_l;
		
		System.out.println("Enter name of Pokemon to edit data on.");
		String name = in.nextLine();
		
		System.out.println("Update Gender on Owned_Pokemon: ");
		gender_g = in.nextLine();
		
		System.out.println("");
		
		System.out.println("Update Level on Owned_Pokemon: ");
		level_l = in.nextInt();
		in.nextLine();
		
		System.out.println("\n");
		
		try { 
			stmt = null;
			stmt = conn.createStatement();
			String s1 = "SELECT distinct_id FROM Owned_Pokemon o, Pokemon p WHERE name = '" + name + "' AND p.pokedex_id = o.pokedex_id;";
			ResultSet r1 = stmt.executeQuery(s1);
			r1.next();
			int distinct_id = r1.getInt("distinct_id");
			
			String SQL = "UPDATE Owned_Pokemon SET gender = '" + gender_g + "', level = " + level_l + " WHERE distinct_id = " + distinct_id + ";";
			stmt.executeUpdate(SQL);
			
			System.out.println("Finished Editing Stats.");
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}

	public static void EditData_2() {
		
		System.out.println("Edit Pokemon Character: ");
		//Scanner input = new Scanner(System.in);
		
		String region_r; 
		int generation_gen;
		int number_owned_pokemon_num;
		int trainer_tr;

		System.out.println("Enter Character ID: ");
		trainer_tr = in.nextInt();
		in.nextLine();
		
		System.out.println("Pokemon Character's Region: ");
		region_r = in.nextLine();
		
		System.out.println("");

		System.out.println("Pokemon Character's Generation: ");
		generation_gen = in.nextInt();
		in.nextLine();
		
		System.out.println("");
		
		System.out.println("How many number of Pokemon owned? ");
		number_owned_pokemon_num = in.nextInt();
		in.nextLine();

		System.out.print("\n");

		try { 
			stmt = null;
			stmt = conn.createStatement();
			String SQL = " UPDATE Character SET region = '" + region_r + "', generation = " + generation_gen + ", number_pokemon_owned = " + number_owned_pokemon_num + " WHERE trainer_id = " + trainer_tr + ";" ;
			stmt.executeUpdate(SQL);
			System.out.println("Finished Updating Character Data.");
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public static void EditData_3() { 
		
		System.out.println("Update Information on where Pokemon were Caught. ");
		//Scanner input = new Scanner(System.in);
		
		String region_reg; 
		String location_loc;
		
		System.out.println("Enter name of Pokemon to edit data on.");
		String name = in.nextLine();
		
		System.out.println("What region were they caught in? ");
		region_reg = in.nextLine();
		
		System.out.print("\n");
		
		System.out.println("Please update the location for the Pokemon: ");
		location_loc = in.nextLine();
		
		System.out.print("\n");
		
		try { 
			stmt = null;
			stmt = conn.createStatement();
			String s1 = "SELECT distinct_id FROM Owned_Pokemon o, Pokemon p WHERE name = '" + name + "' AND p.pokedex_id = o.pokedex_id;";
			ResultSet r1 = stmt.executeQuery(s1);
			r1.next();
			int distinct_id = r1.getInt("distinct_id");
			String SQL = "UPDATE Caught_Location SET region = '" + region_reg + "', location = '" + location_loc + "' WHERE distinct_id = " + distinct_id + ";";
			stmt.executeUpdate(SQL);
			System.out.println("Caught Location Updated.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void EditData_4() {
	
		System.out.println("Edit Static Stats.");
		//Scanner input = new Scanner(System.in);
		
		int HP_hit;
		int Speed_spe; 
		int Attack_att; 
		int Defense_def; 
		int SpecialAttack_satt;
		int SpecialDefense_sdef;
		
		System.out.println("Enter name of Pokemon to edit data on: ");
		String name = in.nextLine();
		
		System.out.println("Option to update the HP stats: ");
		HP_hit = in.nextInt();
		in.nextLine();
		
		System.out.print("\n");
		
		System.out.println("Edit the speed stats: ");
		Speed_spe = in.nextInt();
		in.nextLine();
		
		System.out.print("\n");
		
		System.out.println("Edit the Attack stats: ");
		Attack_att = in.nextInt();
		in.nextLine();
		
		System.out.print("\n");
		
		System.out.println("Edit the defense stats: ");
		Defense_def = in.nextInt();
		in.nextLine();
		
		System.out.print("\n");
		
		System.out.println("Edit the Special Attack stats: ");
		SpecialAttack_satt = in.nextInt();
		in.nextLine();
		
		System.out.println("\n");
		
		System.out.println("Edit the Special Defense stats: "); 
		SpecialDefense_sdef = in.nextInt();
		in.nextLine();
		
		System.out.print("\n");
		
		try { 
			stmt = null;
			stmt = conn.createStatement();
			String s1 = "SELECT distinct_id FROM Owned_Pokemon o, Pokemon p WHERE name = '" + name + "' AND p.pokedex_id = o.pokedex_id;";
			ResultSet r1 = stmt.executeQuery(s1);
			r1.next();
			int distinct_id = r1.getInt("distinct_id");
			String SQL = "UPDATE Owned_Stats SET hp = " + HP_hit + ", speed =  " + Speed_spe + ", attack = " + Attack_att + ", defense = " + Defense_def + ", special_attack = " + SpecialAttack_satt + ", special_defense = " + SpecialDefense_sdef + " WHERE distinct_id = " + distinct_id + ";";
			stmt.executeUpdate(SQL);
			System.out.println("Finished Editing Stats.");
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
	}
	
	public static void EditData_5() {
		System.out.println("Creating a new Pokemon.");
		
		System.out.println("Enter Character ID: ");
		int trainer_id = in.nextInt();
		in.nextLine();
		
		System.out.println("Enter Pokemon Name: ");
		String name = in.nextLine();
		
		System.out.println("Enter Pokemon Gender: ");
		String gender = in.nextLine();
		
		System.out.println("Enter Pokemon Level: ");
		int level = in.nextInt();
		in.nextLine();
		
		System.out.println("Enter Pokemon Ability ID: ");
		int ability_id = in.nextInt();
		in.nextLine();
		
		System.out.println("Enter Caught Location: ");
		String loc = in.nextLine();
		
		System.out.println("Enter Caught Region: ");
		String region = in.nextLine();
		
		System.out.println("Enter HP: ");
		int hp = in.nextInt();
		in.nextLine();
		
		System.out.println("Enter Speed: ");
		int speed = in.nextInt();
		in.nextLine();
		
		System.out.println("Enter Attack: ");
		int attack = in.nextInt();
		in.nextLine();
		
		System.out.println("Enter Defense: ");
		int defense = in.nextInt();
		in.nextLine();
		
		System.out.println("Enter Special Attack: ");
		int sp_attack = in.nextInt();
		in.nextLine();
		
		System.out.println("Enter Special Defense: ");
		int sp_defense = in.nextInt();
		in.nextLine();
		
		try {
			stmt = null;
			stmt = conn.createStatement();
			
			String q1 = "SELECT pokedex_id FROM Pokemon WHERE name = '" + name + "';";
			ResultSet r1 = stmt.executeQuery(q1);
			r1.next();
			int pokedex_id = r1.getInt("pokedex_id");
			
			String q2 = "INSERT INTO Owned_Pokemon VALUES (" + distinct_id_gen + ", " + trainer_id + ", " + pokedex_id + ", '" + gender + "', " + level + ", " + ability_id + ");";
			String q3 = "INSERT INTO Caught_Location VALUES (" + distinct_id_gen + ", " + trainer_id + ", '" + region + "', '" + loc + "');";
			String q4 = "INSERT INTO Owned_Stats VALUES (" + distinct_id_gen + ", " + hp + ", " + speed + ", " + attack + ", " + defense + ", " + sp_attack + ", " + sp_defense + ");";
			String q5 = "INSERT INTO Trainer_Pokemon VALUES (" + distinct_id_gen + ", " + trainer_id + ");";
			
			stmt.executeUpdate(q2);
			stmt.executeUpdate(q3);
			stmt.executeUpdate(q4);
			stmt.executeUpdate(q5);
			System.out.println("Pokemon sucessfully inserted.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		distinct_id_gen++;
	}
	
	public static void EditData_6() {
		System.out.println("Deleting a Pokemon.");
		
		System.out.println("Enter Distinct ID to delete: ");
		int distinct_id = in.nextInt();
		in.nextLine();
		
		try {
			stmt = null;
			stmt = conn.createStatement();
			String q1 = "DELETE FROM Owned_Pokemon WHERE distinct_id = " + distinct_id + ";";
			String q2 = "DELETE FROM Caught_Location WHERE distinct_id = " + distinct_id + ";";
			String q3 = "DELETE FROM Owned_Stats WHERE distinct_id = " + distinct_id + ";";
			String q4 = "DELETE FROM Trainer_Pokemon WHERE distinct_id = " + distinct_id + ";";
			
			stmt.executeUpdate(q1);
			stmt.executeUpdate(q2);
			stmt.executeUpdate(q3);
			stmt.executeUpdate(q4);
			System.out.println("Pokemon sucessfully deleted.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
