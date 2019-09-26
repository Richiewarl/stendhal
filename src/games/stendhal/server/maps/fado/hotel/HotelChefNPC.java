/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2010 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.server.maps.fado.hotel;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import games.stendhal.common.Direction;
import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.SpeakerNPC;

// ORIGINAL WORK comment: Vanessa Julius
/**
 * Builds a NPC in a house on Ados market (name:Stefan) who is the daughter of fisherman Fritz
 *
 * @author Vanessa Julius
 *
 */

// UPDATED COMMENT: (omero)
// Stefan NPC has relocated to Fado's Hotel Restaurant kitchen since some time now.
// Informations about Stefan are desumed from his dialogues
/**
// UPDATED information about Stefan: (omero)
 * Provides Stefan, the Fado's Hotel Restaurant NPC
 * He seems to have moved from Ados market since his original appearence
 * He's now busy in his kitchen, the Fado's Hotel Restaurant
 *
 * original author: Vanessa Julius
 * Stefan has been reworked several times, by various authors
 * Stefan has been relocated from Ados to Fado? It's unclear which authors did what
 *
 * Stefan final known location: Fado's Hotel Restaurant
 * Stefan is currently being involved in quest: Meal For Groongo (MealForGroongo),
 * - relates to TroublesomeCustomerNPC (Groongo)
 * - relates to MealForGroongo (Groongo's quest)
 */

public class HotelChefNPC implements ZoneConfigurator {

	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildNPC(zone);
	}

	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC npc = new SpeakerNPC("Stefan") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(92, 9));
				nodes.add(new Node(98, 9));
	            nodes.add(new Node(98, 2));
	            nodes.add(new Node(93, 2));
	            nodes.add(new Node(93, 4));
	            nodes.add(new Node(91, 4));
	            nodes.add(new Node(91, 3));
	            nodes.add(new Node(90, 3));
	            nodes.add(new Node(90, 11));
	            nodes.add(new Node(98, 11));
	            nodes.add(new Node(98, 9));
	           	setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("Welcome in the Fado hotel kitchen, stranger!");

				addJob(
						"Some weeks ago, I got the job offer to become the Fado's Hotel Restaurant chef... " +
				        "I accepted, of course... " +
						"What I didn't know? I'm now the only #cook *cough* Chef! " +
						"I am the only Chef around in this tiny kitchen at the moment!");

				addReply("cook",
                         "Being a cook is awesome! But I'm a chef! " +
                         "I love all kind of food! Did I tell you I'm a chef, already... Yes I did... " +
                         "Experimenting around with different dishes is just fun for me. I'm a chef, not a cook! " +
                         "Big difference between a cook and a chef, indeed!");

				addReply("chef",
                        "Any cook can prepare a soup... But I'm a chef! " +
                        "I love all kind of food! The elaborate recipes for those troublesome customers... " +
                        "Experimenting around with different dishes is just fun for me. I'm a chef, not a cook! " +
                        "Big difference between a cook and a chef, indeed!");

				addHelp("I'm really #stressed in this kitchen here... " +
				        "I am the only *cough* #cook *cough* #chef *cough* around here... " +
                        "If I only could tell you about all the ingredients that are missing in this place..." +
                        "That #troublesome #customer down there... Keeps ranting and... go ask him what he wants now!");

				addReply("stressed",
                         "It's high season at the moment! " +
				         "We get lots of reservations which means more #guests and more work for everyone.");

				addReply("guest",
						 "Most of the guests visit Fado for #getting #married. " +
				         "I can understand why choosing Fado for getting married. " +
					     "Fado is indeed a beautiful and tranquil, a very romantic city... " +
                         "Except for that troublesome customer down there... " +
					     "That #troublesome #customer keeps ranting... " +
                         "Please, go ask him what he wants now!");

				addReply("troublesome customer",
						 "Most of the guests visit Fado for #getting #married. " +
					     "That troublesome customer instead keeps ranting... " +
                         "Please, go ask him what he wants now, before I go nut!");

				addReply("getting married",
						 "Didn't you know that Fado is the most known wedding town in whole Faiumoni? " +
				         "You have to visit our church, it's so lovely!");

				addQuest("I'm busy at the moment thinking about what I can do to get some help #somewhere...");

				addReply("somewhere",
						 "Yes, somewhere..." +
				         "I doubt that the problem can be solved in my kitchen alone... It's tiny! " +
                         "Hearing that #troublesome #customer down there? He keeps ranting. " +
				         "Quick! Go ask that troublesome customer what he wants, before I go nut...");

				addOffer("The kitchen isn't open at the moment and before it can be opened... " +
                         "I have to think about a solution for my #problem in here... " +
						 "I am a really #stressed #cook now!" );

				addReply("problem", "Being the only #cook... ahem... The only #chef! "+
						 "All alone... In a tiny hotel restaurant kitchen... "+
						 "It will never going to work at all!");

				addGoodbye("Goodbye! Have a nice stay in Fado!");

                /** NOTE:
                 * See MealForGroongo quest for additional informations
                 * src/games/stendhal/server/maps/quests/MealForGroongo.java
                 * Ingredients for preparing main_dish for the troublesome customer
				 */
                /** NOTE:
				 * All ingredients for main_dish  should be trigger words
                 * All ingredients should trigger a reply from Stefan, the chef in Fado's Hotel Restaurant:
                 * All trigger words that give hints about all ingredients needed during MealForGroongo quest
                 * All trigger words for the ingredients needed during MealForGroongo quest are added here
                 */
                /** NOTE:
                 * Some of the ingredients are 'many words'.
                 *     (bunch of)          habanero pepper/peppers?
                 *     (bunch of)          pinto beans
                 *     (bottle/bottles of) olive oil,
                 *     (flask/flasks of)   vinegar,
                 */
                addReply(
                    Arrays.asList(
                    "chicken", "egg", "milk", "butter" ),
                    "Easy... I always check the farming areas near Semos...");
                //dropped by easy critters, goblins, orcs, kalavan housewives, cannibal ladies?
                addReply(
                    Arrays.asList(
                    "vinegar", "olive oil"),
                    "When you're brave enogh, fight! Else go for a grocery store or a market place somewhere not that far... ");
                //in nearby forests, plenty of the stuff
                addReply(
                    Arrays.asList(
                    "porcini", "button mushroom", "sclaria", "kekik"),
                    "Check in some forest, near Semos...");
                //around fado, plenty of the stuff
                addReply(
                    Arrays.asList(
                    "garlic", "onion", "carrot", "courgette"),
                    "Very Easy! Check in Fado surroundings...");
                //the serra near fado gardens?
                addReply(
                    Arrays.asList(
                    "lemon",
                    "potato", "tomato", "pinto beans",
                    "habanero pepper", "habanero peppers"),
                    "Not sure. Maybe near Kalavan gardens...");
                addReply(
                    Arrays.asList(
                    "meat", "cheese", "ham"),
                    "Ehhhrhg... You cannot be that lame!");
                addReply(
                    Arrays.asList(
                    "beer", "flour"),
                    "Ooohrhg... You cannot be that lame!");
                addReply(
                    Arrays.asList(
                    "perch", "trout"),
                    "Ahahah... Nice Try! I will not tell you about my favorite fishing spots...");

                //All ingredients for dessert should be trigger words
                //Ingredients for preparing dessert for the troublesome customer

                addReply(
                    Arrays.asList(
                    "banana", "coconut", "pineapple"),
                    "Exotic fruit... Maybe in a grocery store or market somewhere... " +
                    "You could explore more!");

                addReply(
                    Arrays.asList(
                    "apple", "pear", "lemon", "watermelon"),
                    "Hmm... not such an exotic fruit... Maybe in a grocery store or a market somewhere... " +
                    "You could explore more!");

                addReply(
                    Arrays.asList(
                    "sugar"),
                    "Not so easy to get some in war times! You should either grind some yourself... " +
                    "Or find someone that sells some of it!");
			}
		};

		npc.setDescription("You see Stefan, the young chef of the Fado's Hotel Restaurant.");
		npc.setEntityClass("hotelchefnpc");
		npc.setPosition(92, 9);
		npc.setDirection(Direction.DOWN);
		npc.initHP(100);
		zone.add(npc);
	}
}
