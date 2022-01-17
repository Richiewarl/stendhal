/***************************************************************************
 *                   (C) Copyright 2003-2017 - Stendhal                    *
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU Affero General Public License as        *
 *   published by the Free Software Foundation; either version 3 of the    *
 *   License, or (at your option) any later version.                       *
 *                                                                         *
 ***************************************************************************/

"use strict";

var marauroa = window.marauroa = window.marauroa || {};
var stendhal = window.stendhal = window.stendhal || {};

var ExamineEvent = require("../../build/ts/event/ExamineEvent").ExamineEvent;
var ProgressStatusEvent = require("../../build/ts/event/ProgressStatusEvent").ProgressStatusEvent;
var ui = require("../../build/ts/ui/UI").ui;
var UIComponentEnum = require("../../build/ts/ui/UIComponentEnum").UIComponentEnum;

marauroa.rpeventFactory["attack"] = marauroa.util.fromProto(marauroa.rpeventFactory["_default"], {
	execute: function(entity) {

		var target = entity.getAttackTarget();
		if (!target) {
			return;
		}
		if (this.hasOwnProperty("hit")) {
			var damage = parseInt(this["damage"], 10);
			if (damage !== 0) {
				target.onDamaged(entity, damage);
			} else {
				target.onBlocked(entity);
			}
		} else {
			target.onMissed(entity);
		}
		entity.onAttackPerformed(this["type"], this.hasOwnProperty("ranged"));
	}
});


marauroa.rpeventFactory["examine"] = new ExamineEvent();


marauroa.rpeventFactory["global_visual_effect"] = marauroa.util.fromProto(marauroa.rpeventFactory["_default"], {
	execute: function(rpobject) {
		// TODO: new GlobalVisualEffectEvent();
	}
});


marauroa.rpeventFactory["group_change_event"] = marauroa.util.fromProto(marauroa.rpeventFactory["_default"], {
	execute: function(rpobject) {
		if (rpobject !== marauroa.me) {
			return;
		}
		stendhal.data.group.updateGroupStatus(this["members"], this["leader"], this["lootmode"]);
	}
});


marauroa.rpeventFactory["group_invite_event"] = marauroa.util.fromProto(marauroa.rpeventFactory["_default"], {
	execute: function(rpobject) {
		if (rpobject !== marauroa.me) {
			return;
		}
		if (this["expire"]) {
			Chat.log("normal", "Your group invite by " + this["leader"] + " has expired.");
		} else {
			Chat.log("normal", "Your have been invited by " + this["leader"] + " to join a group.");
			Chat.log("normal", "To join, type: /group join " + this["leader"]);
			Chat.log("normal", "To leave the group at any time, type: /group part " + this["leader"]);
		}
	}
});


marauroa.rpeventFactory["image_event"] = marauroa.util.fromProto(marauroa.rpeventFactory["_default"], {
	execute: function(rpobject) {
		// TODO: new ImageEffectEvent();
		console.log("image_event", this, rpobject);
	}
});


marauroa.rpeventFactory["player_logged_on"] = marauroa.util.fromProto(marauroa.rpeventFactory["_default"], {
	execute: function(rpobject) {
		// TODO: new PlayerLoggedOnEvent();

		// XXX: this can probably be removed if KeyringComponent.update() is fixed
		let features = null;
		if (marauroa.me != null) {
			features = marauroa.me["features"];
		}

		if (features != null) {
			if (features["keyring"] != null) {
				const keyring = ui.get(UIComponentEnum.Keyring);
				if (keyring != null) {
					keyring.setVisible(true);
				}
			}
		}
	}
});


marauroa.rpeventFactory["player_logged_out"] = marauroa.util.fromProto(marauroa.rpeventFactory["_default"], {
	execute: function(rpobject) {
		// TODO: new PlayerLoggedOutEvent();
	}
});


marauroa.rpeventFactory["private_text"] = marauroa.util.fromProto(marauroa.rpeventFactory["_default"], {
	execute: function(rpobject) {
		Chat.log(this["texttype"].toLowerCase(), this["text"]);
	}
});


marauroa.rpeventFactory["progress_status_event"] = new ProgressStatusEvent();

marauroa.rpeventFactory["reached_achievement"] = marauroa.util.fromProto(marauroa.rpeventFactory["_default"], {
	execute: function(rpobject) {
		// TODO: new ReachedAchievementEvent();
	}
});


marauroa.rpeventFactory["show_item_list"] = marauroa.util.fromProto(marauroa.rpeventFactory["_default"], {
	execute: function(rpobject) {
		if (this.hasOwnProperty("title")) {
			Chat.log("normal", this["title"]);
		}
		if (this.hasOwnProperty("caption")) {
			Chat.log("normal", this["caption"]);
		}
		if (this.hasOwnProperty("content")) {
			Chat.log("normal", "Item\t-\tPrice\t-\tDescription");
			for (var obj in this["content"]) {
				if (this["content"].hasOwnProperty(obj)) {
					var slotObj = this["content"][obj];
					var data = this["content"][obj]["a"];
					Chat.log("normal", data["subclass"] + "\t"
							+ data["price"] + "\t" + data["description_info"]);
				}
			}
		}
	}
});


marauroa.rpeventFactory["sound_event"] = marauroa.util.fromProto(marauroa.rpeventFactory["_default"], {
	execute: function(rpobject) {
		var volume = 1;
		// Adjust by the server specified volume, if any
		if (this.hasOwnProperty("volume")) {
			volume *= parseInt(this["volume"], 10) / 100;
		}
		var radius = parseInt(this["radius"], 10);

		stendhal.ui.sound.playLocalizedEffect(rpobject["_x"], rpobject["_y"], radius, this["layer"], this["sound"], volume);
	}
});


marauroa.rpeventFactory["text"] = marauroa.util.fromProto(marauroa.rpeventFactory["_default"], {
	execute: function(rpobject) {
		rpobject.say(this["text"]);
	}
});


marauroa.rpeventFactory["trade_state_change_event"] = marauroa.util.fromProto(marauroa.rpeventFactory["_default"], {
	execute: function(rpobject) {
		// TODO: new TradeStateChangeEvent();
	}
});


marauroa.rpeventFactory["transition_graph"] = marauroa.util.fromProto(marauroa.rpeventFactory["_default"], {
	execute: function(rpobject) {
		// TODO: new TransitionGraphEvent();
	}
});


marauroa.rpeventFactory["view_change"] = marauroa.util.fromProto(marauroa.rpeventFactory["_default"], {
	execute: function(rpobject) {
		// TODO: new ViewChangeEvent();
	}
});
// Dummy comment to prevent accidental re-push of a rebase done into the wrong direction

marauroa.rpeventFactory["bestiary"] = marauroa.util.fromProto(marauroa.rpeventFactory["_default"], {
	execute: function(rpobject) {
		if (!this.hasOwnProperty("enemies")) {
			// FIXME: proper logging of errors?
			console.log("ERROR: event does not have \"enemies\" attribute");
			return;
		}

		const title = "Bestiary";
		var header = ["\"???\" = unknown"];

		const hasRare = this["enemies"].includes("(rare)");
		const hasAbnormal = this["enemies"].includes("(abnormal)");

		// show explanation of "rare" & "abnormal" creatures in header
		if (hasRare || hasAbnormal) {
			var subheader = "";
			if (!hasRare) {
				subheader += "\"abnormal\"";
			} else {
				subheader += "\"rare\"";
				if (hasAbnormal) {
					subheader += " and \"abnormal\"";
				}
			}

			header[1] = subheader + " creatures not required for achievements";
		}

		// spacing for clarity
		header[2] = "------------------";

		// FIXME: hack until a proper window is implemented
		Chat.log("normal", title + ":");
		for (h of header) {
			Chat.log("normal", h);
		}

		const enemies = this["enemies"].split(";");
		for (e of enemies) {
			const info = e.split(",");
			const name = info[0];
			var solo = " ";
			var shared = " ";
			if (info[1] == "true") {
				solo = "✔";
			}
			if (info[2] == "true") {
				shared = "✔";
			}

			// FIXME: hack until a proper window is implemented
			Chat.log("normal", name + ":   solo [" + solo + "], shared [" + shared + "]");
		}
	}
});
