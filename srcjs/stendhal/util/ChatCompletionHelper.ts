/***************************************************************************
 *                    Copyright © 2024 - Faiumoni e. V.                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU Affero General Public License as        *
 *   published by the Free Software Foundation; either version 3 of the    *
 *   License, or (at your option) any later version.                       *
 *                                                                         *
 ***************************************************************************/

declare var marauroa: any;
declare var stendhal: any;

import { SlashActionRepo } from "../SlashActionRepo";

import { ui } from "../ui/UI";
import { UIComponentEnum } from "../ui/UIComponentEnum";

import { ChatInputComponent } from "../ui/component/ChatInputComponent";


export class ChatCompletionHelper {

	/** Chat commands relating to players (tab completes player names). */
	private readonly playerCommands: string[] = [
		"add", "adminlevel", "alter", "alterkill", "alterquest",
		"ban",
		"gag",
		"ignore", "inspect", "inspectkill", "inspectquest",
		"jail",
		"msg",
		"profile",
		"remove",
		"summonat", "supporta", "supportanswer",
		"teleport", "teleportto", "tell",
		"unignore",
		"where"
	];

	/** Chat commands available only for admins/GMs. */
	private readonly adminCommands: string[] = [
		"adminnote", "alter", "altercreature", "alterkill", "alterquest",
		"ban",
		"destroy",
		"gag", "ghostmode",
		"inspect", "inspectkill", "inspectquest", "invisible",
		"jail", "jailreport",
		"script", "summon", "summonat", "supporta", "supportanswer",
		"teleclickmode", "teleport", "teleportto", "tellall"
	];

	/** Chat commands not explicitly registered in SlashActionRepo. */
	private readonly unlistedCommands: string[] = [
		"destroy",
		"ghostmode",
		"info", "inspect", "invisible",
		"jailreport",
		"markscroll",
		"name",
		//"removedetail", // don't include this to prevent accidental loss of detail layer
		"script",
		"teleclickmode",
		"unignore",
		"where", "who"
	];

	/** Available chat commands. */
	private readonly chatCommands: string[] = [];

	private commandPrefix?: string;
	private commandIndex = -1;
	private playerPrefix?: string;
	private playerIndex = -1;

	/** Singleton instance. */
	private static instance: ChatCompletionHelper;


	/**
	 * Retrieves the singleton instance.
	 */
	static get(): ChatCompletionHelper {
		if (!ChatCompletionHelper.instance) {
			ChatCompletionHelper.instance = new ChatCompletionHelper();
		}
		return ChatCompletionHelper.instance;
	}

	/**
	 * Hidden singleton constructor.
	 */
	private constructor() {
		// do nothing
	}

	/**
	 * Called when tab key is pressed while chat input has focus.
	 *
	 * FIXME: have to press tab twice at end of commands list
	 */
	onTabKey() {
		const parts: string[] = [];
		const chatInput = (ui.get(UIComponentEnum.ChatInput) as ChatInputComponent);
		for (const p of chatInput.getText().split(" ")) {
			parts.push(p);
		}
		if (parts.length == 0 || !parts[0].startsWith("/")) {
			return;
		}
		this.parseChatCommands();
		if (this.commandPrefix == undefined) {
			this.commandPrefix = parts[0].substring(1, parts[0].length);
		}
		this.commandIndex++;
		if (this.commandIndex > this.chatCommands.length - 1) {
			this.commandIndex = 0;
		}
		if (parts.length < 2) {
			for (this.commandIndex; this.commandIndex < this.chatCommands.length; this.commandIndex++) {
				const cmd = this.chatCommands[this.commandIndex];
				if (cmd.startsWith(this.commandPrefix)) {
					chatInput.setText("/" + cmd);
					break;
				}
			}
			return;
		}
		if (!stendhal.players || parts.length > 2 || this.playerCommands.indexOf(this.commandPrefix) < 0) {
			return;
		}
		if (this.playerPrefix == undefined) {
			this.playerPrefix = parts[1];
		}
		this.playerIndex++;
		if (this.playerIndex > stendhal.players.length - 1) {
			this.playerIndex = 0;
		}
		for (this.playerIndex; this.playerIndex < stendhal.players.length; this.playerIndex++) {
			const name = stendhal.players[this.playerIndex];
			if (name.startsWith(this.playerPrefix)) {
				parts[parts.length-1] = name;
				break;
			}
		}
		chatInput.setText(parts.join(" "));
	}

	/**
	 * Extracts usable chat commands from slash action repository.
	 */
	private parseChatCommands() {
		if (this.chatCommands.length > 0) {
			return;
		}
		const excludes: string[] = ["/", "_default"];
		const admin = marauroa.me && marauroa.me.isAdmin();
		for (const cmd of [...Object.getOwnPropertyNames(SlashActionRepo.get()), ...this.unlistedCommands].sort()) {
			if (excludes.indexOf(cmd) > -1 || (this.adminCommands.indexOf(cmd) > -1 && !admin)) {
				continue;
			}
			this.chatCommands.push(cmd);
		}
	}

	/**
	 * Resets command & player prefixes to default values.
	 */
	reset() {
		this.commandPrefix = undefined;
		this.commandIndex = -1;
		this.playerPrefix = undefined;
		this.playerIndex = -1;
	}
}
