package games.stendhal.server.maps.quests;

import games.stendhal.server.StendhalRPWorld;
import games.stendhal.server.StendhalRPZone;
import games.stendhal.server.entity.Player;
import games.stendhal.server.entity.item.Corpse;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.pathfinder.Path;
import games.stendhal.server.scripting.ScriptAction;
import games.stendhal.server.scripting.ScriptCondition;
import games.stendhal.server.scripting.ScriptInGroovy;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import marauroa.common.game.IRPZone;
import marauroa.common.game.RPSlot;


/**
 * QUEST: 
 * Creates a 6 step quest in Kanmararn
 * Note: it also starts a quest that needs NPC McPegleg that is created.
 * It doesn't harm if that script is missing, just that the IOU cannot be
 * delivered and hence the player can't get cash
 *
 * PARTICIPANTS:
 *
 * STEPS:
 *
 * REWARD:
 *
 * REPETITIONS:
 * - None.
 */
public class KanmararnSoldiers extends AbstractQuest {

	private static final String QUEST_SLOT = "soldier_henry";

	@Override
	public void init(String name) {
		super.init(name, QUEST_SLOT);
	}
	
	class CorpseEmptyCondition extends ScriptCondition {
		Corpse corpse;
		
		public CorpseEmptyCondition (Corpse corpse) {
			this.corpse = corpse;
		}
		
		public boolean fire() {
			return corpse.size() < 1;
		}
	}

	class CorpseFillAction extends ScriptAction {
		Corpse corpse;
		ScriptInGroovy game;
		String itemName;
		String description;
		public CorpseFillAction (Corpse corpse, ScriptInGroovy game, String itemName, String description) {
			this.corpse = corpse;
			this.game = game;
			this.itemName = itemName;
			this.description = description;
		}
		
		public void fire() {
			Item item = game.getItem(itemName);
			item.put("infostring", corpse.get("name"));
			item.setDescription(description);
			corpse.add(item);
		}
	}

	class HenryQuestAction extends SpeakerNPC.ChatAction {
		public void fire(Player player, String text, SpeakerNPC engine) {
			if(!player.isQuestCompleted("soldier_henry") && !"map".equals(player.getQuest("soldier_henry"))) {
				engine.say("Find my #group, Peter, Tom and Charles, prove it and I will reward you. Will you do it?");
			} else {
				engine.say("I'm so sad that most of my friends are dead.");
				engine.setCurrentState(1);
			}
		}
	}

	class HenryQuestAcceptAction extends SpeakerNPC.ChatAction {
		public void fire(Player player, String text, SpeakerNPC engine) {
			player.setQuest("soldier_henry","start");
		}
	}

	class HenryQuestCompleteCondition extends SpeakerNPC.ChatCondition {
		public boolean fire(Player player, SpeakerNPC engine) {
			return (player.hasQuest("soldier_henry") && player.getQuest("soldier_henry").equals("start"));
		}
	}

	class HenryQuestCompletedCondition extends SpeakerNPC.ChatCondition {
		public boolean fire(Player player, SpeakerNPC engine) {
			return (player.hasQuest("soldier_henry") && !player.getQuest("soldier_henry").equals("start"));
		}
	}

	class HenryQuestCompleteAction extends SpeakerNPC.ChatAction {
		public void fire(Player player, String text, SpeakerNPC engine) {

			List<Item> allLeatherLegs = player.getAllEquipped("leather_legs");
			Item questLeatherLegs = null;
			for (Item leatherLegs : allLeatherLegs) {
				if (leatherLegs.has("infostring") && "tom".equalsIgnoreCase(leatherLegs.get("infostring"))) {
					questLeatherLegs = leatherLegs;
					break;
				}
			}

			List<Item> allNotes	= player.getAllEquipped("note");
			Item questNote = null;
			for (Item note : allNotes) {
				if (note.has("infostring") && "charles".equalsIgnoreCase(note.get("infostring"))) {
					questNote = note;
					break;
				}
			}
			
			List<Item> allScaleArmors = player.getAllEquipped("scale_armor");
			Item questScaleArmor = null;
			for (Item scaleArmor : allScaleArmors) {
				if (scaleArmor.has("infostring") && "peter".equalsIgnoreCase(scaleArmor.get("infostring"))) {
					questScaleArmor = scaleArmor;
					break;
				}
			}

			if(questLeatherLegs != null && questNote != null && questScaleArmor != null) {
				engine.say("Oh my! Peter, Tom and Charles are all dead? *cries*. Anyway, here is your reward. And keep the IOU.");
				player.addXP(2500);
				player.drop(questLeatherLegs);
				player.drop(questScaleArmor);
				Item map = StendhalRPWorld.get().getRuleManager().getEntityManager().getItem("map");
				map.put("infostring", engine.get("name"));
				map.setDescription("You see a hand drawn map, but no matter how you look at it, nothing on it looks familiar.");
				RPSlot slot=player.getSlot("bag");
				slot.add(map);
				player.setQuest("soldier_henry","map");
				engine.setCurrentState(1);
			} else {
				engine.say("You didn't prove that you have found them all!");
			}
		}
	}

	class JamesQuestCompleteCondition extends SpeakerNPC.ChatCondition {
		public boolean fire(Player player, SpeakerNPC engine) {
			return (player.hasQuest("soldier_henry") && player.getQuest("soldier_henry").equals("map"));
		}
	}

	class JamesQuestCompletedCondition extends SpeakerNPC.ChatCondition {
		public boolean fire(Player player, SpeakerNPC engine) {
			return (player.isQuestCompleted("soldier_henry"));
		}
	}

	class JamesQuestCompleteAction extends SpeakerNPC.ChatAction {
		public void fire(Player player, String text, SpeakerNPC engine) {
		
			List<Item> allMaps = player.getAllEquipped("map");
			Item questMap = null;
			for (Item map : allMaps) {
				if (map.has("infostring") && "henry".equalsIgnoreCase(map.get("infostring"))) {
					questMap = map;
					break;
				}
			}
			if (questMap != null) {
				engine.say("The map! Wonderful! Thank you. And here is your reward.");
				player.addXP(5000);
				player.drop(questMap);
				
				Item item = StendhalRPWorld.get()
				.getRuleManager().getEntityManager().getItem(
						"steel_boots");
				item.put("infostring", engine.get("name"));
				RPSlot slot=player.getSlot("bag");
				slot.add(item);
				player.setQuest("soldier_henry","done");
				engine.setCurrentState(1);
			} else {
				engine.say("Well, where is the map?");
			}
		}
	}

	private void step_1() {
		StendhalRPZone zone = (StendhalRPZone) StendhalRPWorld.get().getRPZone(new IRPZone.ID("-6_kanmararn_city"));
//		 The quest is set in the first level of Kanmararn
		// We create NPC Henry who will get us on the quest
		SpeakerNPC henry = new SpeakerNPC("Henry") {

			@Override
			protected void createDialog() {
				List<Path.Node> nodes = new LinkedList<Path.Node>();
				nodes.add(new Path.Node(57, 112));
				nodes.add(new Path.Node(59, 112));
				nodes.add(new Path.Node(59, 114));
				setPath(nodes, true);
			}

			@Override
			protected void createPath() {
				// Adds all the behaviour chat
				addGreeting("Ssshh! Silence or you will attract more #dwarves.");
				addJob("I'm a soldier in the army.");
				addGoodbye("Bye and be careful with all those dwarves around!");
				add(ConversationStates.ATTENDING, Arrays.asList("dwarf", "dwarves"), ConversationStates.ATTENDING, "They are everywhere! Their #kingdom must be close.", null);
				add(ConversationStates.ATTENDING, Arrays.asList("kingdom", "Kanmararn"), ConversationStates.ATTENDING, "Kanmararn, the legendary city of the #dwarves.", null);

				addHelp("I need help myself. I got seperated from my #group. Now I'm all alone.");
				add(ConversationStates.ATTENDING, Arrays.asList("group"), ConversationStates.ATTENDING, "The General sent five of us to explore this area in search for #treasure.", null);
				add(ConversationStates.ATTENDING, Arrays.asList("treasure"), ConversationStates.ATTENDING, "A big treasure is rumored to be #somewhere in this dungeon.", null);
				add(ConversationStates.ATTENDING, Arrays.asList("somewhere"), ConversationStates.ATTENDING, "If you #help me I might give you a clue.", null);
				// Add the quest dependent chat
				add(ConversationStates.ATTENDING, Arrays.asList("quest", "task"), null, ConversationStates.QUEST_OFFERED, null, new HenryQuestAction());
				add(ConversationStates.QUEST_OFFERED, SpeakerNPC.YES_MESSAGES,null, ConversationStates.ATTENDING, "Thank you! I'll be waiting for your return.", new HenryQuestAcceptAction());
				add(ConversationStates.QUEST_OFFERED, "group", null, ConversationStates.QUEST_OFFERED, "The General sent five of us to explore this area in search for #treasure.", null);
				add(ConversationStates.QUEST_OFFERED, "no", null, ConversationStates.ATTENDING, "Ok. I understand. I'm scared of the #dwarves myself.", null);
				add(ConversationStates.IDLE, Arrays.asList("hi", "hello", "greetings", "hola"), new HenryQuestCompleteCondition(), ConversationStates.ATTENDING, null, new HenryQuestCompleteAction());
				add(ConversationStates.ATTENDING, Arrays.asList("map", "group", "help"), new HenryQuestCompletedCondition(), ConversationStates.ATTENDING, "I'm so sad that most of my friends are dead.", null);
			}
		};

		// Adjust level/hp and add our new NPC to the game world
		henry.setLevel(5);
		henry.setHP(henry.getBaseHP() * 20 / 100);

		henry.put("class", "youngsoldiernpc");
		henry.setDescription("You see a young soldier who appears to be afraid.");
		npcs.add(henry);
		zone.assignRPObjectID(henry);
		zone.addNPC(henry);
	}
/*	
	private void step_2() {
		// Now we create the corpse of the second NPC
		Corpse tom = new QuestKanmararn.QuestCorpse("youngsoldiernpc", 5, 47);
		tom.setDegrading(false);
		tom.setStage(4);	// he died first
		tom.put("name", "Tom");
		tom.put("killer", "a Dwarven patrol");
		// Add our new Ex-NPC to the game world
		game.add(tom);
		// Add a script to automatically fill the corpse of unlucky Tom
		game.add(new CorpseEmptyCondition(tom), 
				new CorpseFillAction(tom, game, "leather_legs", "You see torn leather legs that are heavily covered with blood."));

		// Now we create the corpse of the third NPC
		Corpse charles = new QuestKanmararn.QuestCorpse("youngsoldiernpc", 94, 5);
		charles.setDegrading(false);
		charles.setStage(3);	// he died second
		charles.put("name", "Charles");
		charles.put("killer", "a Dwarven patrol");
		// Add our new Ex-NPC to the game world
		game.add(charles);
		// Add a script to automatically fill the corpse of unlucky Charles
		game.add(new CorpseEmptyCondition(charles), 
				new CorpseFillAction(charles, game, "note", "You read: \"IOU 250 gold. (signed) McPegleg\""));
	 
		// Now we create the corpse of the fourth NPC
		Corpse peter = new QuestKanmararn.QuestCorpse("youngsoldiernpc", 11, 63);
		peter.setDegrading(false);
		peter.setStage(2);	// he died recently
		peter.put("name", "Peter");
		peter.put("killer", "a Dwarven patrol");
		// Add our new Ex-NPC to the game world
		game.add(peter);
		// Add a script to automatically fill the corpse of unlucky Peter
		game.add(new CorpseEmptyCondition(peter), 
				new CorpseFillAction(peter, game, "scale_armor", "You see a slightly rusty scale armor. It is heavily deformed by several strong hammer blows."));
	}
*/
	private void step_3() {
		StendhalRPZone zone = (StendhalRPZone) StendhalRPWorld.get().getRPZone(new IRPZone.ID("-6_kanmararn_city"));

		// We create NPC James, the chief and last survivor of the quintet
		SpeakerNPC james = new SpeakerNPC("Sergeant James") {
	
			@Override
			protected void createDialog() {
				List<Path.Node> nodes = new LinkedList<Path.Node>();
				nodes.add(new Path.Node(66, 45));
				nodes.add(new Path.Node(66, 47));
				setPath(nodes, true);
			}
	
			@Override
			protected void createPath() {
				// Adds all the behaviour chat
				addGreeting("Good day, adventurer!");
				addJob("I'm a Sergeant in the army.");
				addGoodbye("Good luck and better watch your back with all those dwarves around!");

				// quest related stuff
				addHelp("Think I need a little help myself. My #group got killed and #one of my men ran away. Too bad he had the #map.");
				addQuest("Find my fugitive soldier and bring him to me ... or at least the #map he's carrying.");
				add(ConversationStates.ATTENDING, Arrays.asList("group"), ConversationStates.ATTENDING, "We were five, three of us died. You probably passed their corpses.", null);
				add(ConversationStates.ATTENDING, Arrays.asList("one", "henry"), ConversationStates.ATTENDING, "Yes, my youngest soldier. He ran away.", null);
				add(ConversationStates.ATTENDING, Arrays.asList("map"), ConversationStates.ATTENDING, "The #treasure map that leads into the heart of the #dwarven #kingdom.", null);
				add(ConversationStates.ATTENDING, Arrays.asList("treasure"), ConversationStates.ATTENDING, "A big treasure is rumored to be somewhere in this dungeon.", null);
				add(ConversationStates.ATTENDING, Arrays.asList("dwarf", "dwarves", "dwarven"), ConversationStates.ATTENDING, "They are strong enemies! We're in their #kingdom.", null);
				add(ConversationStates.ATTENDING, Arrays.asList("peter", "tom", "charles"), ConversationStates.ATTENDING, "He was a good soldier and fought bravely.", null);
				add(ConversationStates.ATTENDING, Arrays.asList("kingdom", "Kanmararn"), ConversationStates.ATTENDING, "Kanmararn, the legendary kingdom of the #dwarves.", null);

				add(ConversationStates.ATTENDING, Arrays.asList("map", "henry"), new JamesQuestCompleteCondition(), 1, null, new JamesQuestCompleteAction());
				add(ConversationStates.ATTENDING, Arrays.asList("map", "henry", "quest", "task", "help", "group", "one"), new JamesQuestCompletedCondition(), ConversationStates.ATTENDING, "Thanks again for bringing me the map!", null);
			}
		};

		// Adjust level/hp and add our new NPC to the game world
		james.setLevel(20);
		james.setHP(james.getBaseHP() * 75 / 100);

		james.setDescription("You see an officer who bears many signs of recent battles.");
		james.put("class", "royalguardnpc");
		npcs.add(james);
		zone.assignRPObjectID(james);
		zone.addNPC(james);
	}
	@Override
	public void addToWorld() {
		super.addToWorld();

		step_1();
//		step_2();
		step_3();
	}


}
