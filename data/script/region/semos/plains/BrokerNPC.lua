--[[
 ***************************************************************************
 *                    Copyright © 2022-2023 - Arianne                      *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************
]]


-- an accessible NPC for low levels that purchases misc. items for a lower price


local zone_name = "0_semos_plains_n"

if game:setZone(zone_name) then
	local broker = entities:create({
		type = "SpeakerNPC",
		name = "Emeric",
		outfit = {
			layers = "body=0,head=0,eyes=0,hair=32,dress=13",
			colors = {
				eyes = 0x468499,
				dress = 0x065535
			}
		},
		pos = {60, 92},
		idleDir = Direction.RIGHT
	})

	broker:addGreeting();
	broker:addGoodbye();
	broker:addJob("I am a broker that buys miscellaneous items and sells to shops around Faimouni.")
	broker:addQuest("There is nothing I need help with, other than gathering materials to make a profit " ..
		"off shops around Faimouni.")
	broker:addHelp("I will buy certain items from you at a very reasonable price.... for me.")

	game:add(broker)
else
	logger:error("could not set zone: " .. zone_name)
end
