
public final class NPCDef {

	public int frontLight = 68;
	public int backLight = 820;
	public int rightLight = 0;
	public int middleLight = -1; // Cannot be 0
	public int leftLight = 0;

	public static NPCDef forID(int i) {
		for (int j = 0; j < 20; j++)
			if (cache[j].type == (long) i)
				return cache[j];
		cacheIndex = (cacheIndex + 1) % 20;
		NPCDef npc = cache[cacheIndex] = new NPCDef();
		if (i >= streamIndices.length)
			return null;
		stream.currentOffset = streamIndices[i];
		npc.type = i;
		npc.readValues(stream);
		if(npc.name != null && npc.name.toLowerCase().contains("bank")) {
			if(npc.actions != null) {
				for(int l = 0; l < npc.actions.length; l++) {
					if(npc.actions[l] != null && npc.actions[l].equalsIgnoreCase("Collect"))
						npc.actions[l] = null;
				}
			}
		}
		npc.id = i;
		switch (i) {
		case 2000:
			npc.models = new int[2];
			npc.models[0] = 28294;
			npc.models[1] = 28295;
			npc.name = "Venenatis";
			npc.actions = new String[] {null, "Attack", null, null, null};
			npc.sizeXZ = 200;
			npc.sizeY = 200;
			NPCDef ven = forID(60);
			npc.standAnim = ven.standAnim;
			npc.walkAnim = ven.walkAnim;
			npc.combatLevel = 464;
			npc.squaresNeeded = 3;
			break;
		case 2001:
			npc.models = new int[1];
			npc.models[0] = 28293;
			npc.name = "Scorpia";
			npc.actions = new String[] {null, "Attack", null, null, null};
			NPCDef scor = forID(107);
			npc.standAnim = scor.standAnim;
			npc.walkAnim = scor.walkAnim;
			npc.combatLevel = 464;
			npc.squaresNeeded = 3;
			break;
		case 2002:
			npc.models = new int[1];
			npc.models[0] = 28299;
			npc.name = "Vet'ion";
			npc.actions = new String[] {null, "Attack", null, null, null};
			NPCDef vet = forID(90);
			npc.standAnim = vet.standAnim;
			npc.walkAnim = vet.walkAnim;
			npc.combatLevel = 464;
			break;
		case 2003:
			npc.models = new int[1];
			npc.models[0] = 28281;
			npc.name = "Kraken";
            npc.actions = new String[] {null, "Attack", null, null, null};
            NPCDef eld = forID(3847);
            npc.models = new int[1];
            npc.models[0] = 28233;
            npc.combatLevel = 291;
            npc.standAnim = 3989;
            npc.walkAnim = eld.walkAnim;
            npc.sizeXZ = npc.sizeY = 84;
            break;
		case 2004:
			npc.models = new int[1];
			npc.models[0] = 28231;
			npc.name = "Cave kraken";
			npc.actions = new String[] {null, "Attack", null, null, null};
			NPCDef cave = forID(3847);
			npc.models = new int[1];
			npc.models[0] = 28233;
			npc.combatLevel = 127;
			npc.standAnim = 3989;
			npc.walkAnim = cave.walkAnim;
			npc.sizeXZ = npc.sizeY = 42;
			break;
		case 457:
			npc.name = "Ghost Town Citizen";
			npc.actions = new String[]{"Talk-to", null, "Teleport", null, null};
			break;
		case 5417:
			npc.combatLevel = 210;
			break;
		case 5418:
			npc.combatLevel = 90;
			break;
		case 6715:
			npc.combatLevel = 91;
			break;
		case 6716:
			npc.combatLevel = 128;
			break;
		case 6701:
			npc.combatLevel = 173;
			break;
		case 6725:
			npc.combatLevel = 224;
			break;
		case 6691:
			npc.squaresNeeded = 2;
			npc.combatLevel = 301;
			break;
		case 8710:
		case 8707:
		case 8706:
		case 8705:
			npc.name = "Musician";
			npc.actions = new String[]{"Listen-to", null, null, null, null};
			break;
		case 947:
			npc.name = "Grand Exchange clerk";
			break;
		case 9939:
			npc.combatLevel = 607;
			break;
		case 688:
			npc.name = "Archer";
			break;
		case 4540:
			npc.combatLevel = 299;
			break;
		case 3101:
			npc.sizeY = npc.sizeXZ = 80;
			npc.squaresNeeded = 1;
			npc.actions = new String[]{"Talk-to", null, "Start", "Rewards", null};
			break;
		case 6222:
			npc.name = "Kree'arra";
			npc.squaresNeeded = 5;
			npc.standAnim = 6972;
			npc.walkAnim = 6973;
			npc.actions = new String[]{null, "Attack", null, null, null};
			npc.sizeY = npc.sizeXZ = 110;
			break;
		case 6203:
			npc.models = new int[] {27768, 27773, 27764, 27765, 27770};
			npc.name = "K'ril Tsutsaroth";
			npc.squaresNeeded = 5;
			npc.standAnim = 6943;
			npc.walkAnim = 6942;
			npc.actions = new String[]{null, "Attack", null, null, null};
			npc.sizeY = npc.sizeXZ = 110;
			break;
		case 1610:
		case 491:
		case 10216:
			npc.actions = new String[]{null, "Attack", null, null, null};
			break;
		case 7969:
			npc.actions = new String[]{"Talk-to", null, "Trade", null, null};
			break;
		case 1382:
			npc.name = "Glacor";
			npc.models = new int[]{58940};
			npc.squaresNeeded = 3;
			//	npc.anInt86 = 475;
			npc.sizeXZ = npc.sizeY = 180;
			npc.standAnim = 10869;
			npc.walkAnim = 10867;
			npc.actions = new String[]{null, "Attack", null, null, null};
			npc.combatLevel = 123;
			npc.drawMinimapDot = true;
			npc.combatLevel = 188;
			break;
			/*case 1383:
			npc.name = "Unstable glacyte";
			npc.models = new int[]{58942};
			npc.standAnim = 10867;
			npc.walkAnim = 10901;
			npc.actions = new String[]{null, "Attack", null, null, null};
			npc.combatLevel = 101;
			npc.drawMinimapDot = false;
			break;
		case 1384:
			npc.name = "Sapping glacyte";
			npc.models = new int[]{58939};
			npc.standAnim = 10867;
			npc.walkAnim = 10901;
			npc.actions = new String[]{null, "Attack", null, null, null};
			npc.combatLevel = 101;
			npc.drawMinimapDot = true;
			break;
		case 1385:
			npc.name = "Enduring glacyte";
			npc.models = new int[]{58937};
			npc.standAnim = 10867;
			npc.walkAnim = 10901;
			npc.actions = new String[]{null, "Attack", null, null, null};
			npc.combatLevel = 101;
			npc.drawMinimapDot = true;
			break;*/
		case 4249:
			npc.name = "Gambler";
			break;
		case 6970:
			npc.actions = new String[] {"Trade", null, "Exchange Shards", null, null};
			break;
		case 4657:
			npc.actions = new String[] {"Talk-to", null, "Claim Items", "Check Total", "Teleport"};
			break;
		case 605:
			npc.actions = new String[] {"Talk-to", null, "Vote Rewards", "Loyalty Titles", null};
			break;
		case 8591:
			npc.actions = new String[] {"Talk-to", null, "Trade", null, null};
			break;
		case 316:
		case 315:
		case 309:
		case 310:
		case 314:
		case 312:
		case 313:
			npc.sizeXZ = 30;
			break;
		case 318:
			npc.sizeXZ = 30;
			npc.actions = new String[] {"Net", null, "Lure", null, null};
			break;
		case 805:
			npc.actions = new String[] {"Trade", null, "Tan hide", null, null};
			break;
		case 461:
		case 844:
		case 650:
		case 5112:
		case 3789:
		case 802:
		case 520:
		case 521:
		case 11226:
			npc.actions = new String[] {"Trade", null, null, null, null};
			break;
		case 8022:
		case 8028:
			String color = i == 8022 ? "Yellow" : "Green";
			npc.name = ""+color+" energy source";
			npc.actions = new String[] {"Siphon", null, null, null, null};
			break;
		case 8444:
			npc.actions = new String[5];
			npc.actions[0] = "Trade";
			break;
		case 2579:
			npc.name = "Veteran";
			npc.description = "One of Ruse's veterans.";
			npc.combatLevel = 200;
			npc.actions = new String[5];
			npc.actions[0] = "Talk-to";
			npc.actions[2] = "Trade";
			npc.models = new int[7];
			npc.models[0] = 65289;
			npc.models[1] = 62746;
			npc.models[2] = 62743;
			npc.models[3] = 65305;
			npc.models[4] = 13319;
			npc.models[5] = 27738;
			npc.models[6] = 20147;
			npc.standAnim = 808;
			npc.walkAnim = 819;
			npc.npcHeadModels = NPCDef.forID(517).npcHeadModels;
			break;
		case 6830:
		case 6841:
		case 6796:
		case 7331:
		case 6831:
		case 7361:
		case 6847:
		case 6872:
		case 7353:
		case 6835:
		case 6845:
		case 6808:
		case 7370:
		case 7333:
		case 7351:
		case 7367:
		case 6853:
		case 6855:
		case 6857:
		case 6859:
		case 6861:
		case 6863:
		case 9481:
		case 6827:
		case 6889:
		case 6813:
		case 6817:
		case 7372:
		case 6839:
		case 8575:
		case 7345:
		case 6799:
		case 7335:
		case 7347:
		case 6800:
		case 9488:
		case 6804:
		case 6822:
		case 6849:
		case 7355:
		case 7357:
		case 7359:
		case 7341:
		case 7329:
		case 7339:
		case 7349:
		case 7375:
		case 7343:
		case 6820:
		case 6865:
		case 6809:
		case 7363:
		case 7337:
		case 7365:
		case 6991:
		case 6992:
		case 6869:
		case 6818:
		case 6843:
		case 6823:
		case 7377:
		case 6887:
		case 6885:
		case 6883:
		case 6881:
		case 6879:
		case 6877:
		case 6875:
		case 6833:
		case 6851:
		case 5079:
		case 5080:
		case 6824:
			npc.actions = new String[] {null, null, null, null, null};
			break;
		case 6806: // thorny snail
		case 6807:
		case 6994: // spirit kalphite
		case 6995:
		case 6867: // bull ant
		case 6868:
		case 6794: // spirit terrorbird
		case 6795:
		case 6815: // war tortoise
		case 6816:
		case 6874:// pack yak
		case 6873: // pack yak
		case 3594: // yak
		case 3590: // war tortoise
		case 3596: // terrorbird
			npc.actions = new String[] {"Store", null, null, null, null};
			break;
		case 548:
			npc.actions = new String[] {"Trade", null, null, null, null};
			break;
		case 3299:
		case 437:
			npc.actions = new String[] {"Trade", null, null, null, null};
			break;
		case 1265:
		case 1267:
		case 8459:
			npc.drawMinimapDot = true;
			break;
		case 961:
			npc.actions = new String[] {null, null, "Buy Consumables", "Restore Stats", null};
			npc.name = "Healer";
			break;
		case 705:
			npc.actions = new String[] {null, null, "Buy Armour", "Buy Weapons", "Buy Jewelries"};
			npc.name = "Warrior";
			break;
		case 1861:
			npc.actions = new String[] {null, null, "Buy Equipment", "Buy Ammunition", null};
			npc.name = "Archer";
			break;
		case 946:
			npc.actions = new String[] {null, null, "Buy Equipment", "Buy Runes", null};
			npc.name = "Mage";
			break;
		case 2253:
			npc.actions = new String[] {null, null, "Buy Skillcapes", "Buy Skillcapes (t)", "Buy Hoods"};
			break;
		case 2292:
			npc.actions = new String[] {"Trade", null, null, null, null};
			npc.name = "Merchant";
			break;
		case 2676:
			npc.actions = new String[] {"Makeover", null, null, null, null};
			break;
		case 494:
		case 1360:
			npc.actions = new String[] {"Talk-to", null, null, null, null};
			break;
		case 1685:
			npc.name = "Pure";
			npc.actions = new String[] {"Trade", null, null, null, null};
			break;
		case 3030:
			npc.name = "King black dragon";
			npc.actions = new String[5];
			npc.actions[0] = "Pick-up";
			npc.models = new int[] {17414, 17415, 17429, 17422};
			npc.combatLevel = 276;
			npc.standAnim = 90;
			npc.walkAnim = 4635;
			npc.sizeY = 63;
			npc.sizeXZ = 63;
			npc.squaresNeeded = 3;
			break;

		case 3031:
			npc.name = "General graardor";
			npc.actions = new String[5];
			npc.actions[0] = "Pick-up";
			npc.models = new int[] {27785, 27789};
			npc.combatLevel = 624;
			npc.standAnim = 7059;
			npc.walkAnim = 7058;
			npc.sizeY = 40;
			npc.sizeXZ = 40;
			break;	

		case 3032:
			npc.name = "TzTok-Jad";
			npc.actions = new String[5];
			npc.actions[0] = "Pick-up";
			npc.models = new int[] {34131};
			npc.combatLevel = 702;
			npc.standAnim = 9274;
			npc.walkAnim = 9273;
			npc.sizeY = 45;
			npc.sizeXZ = 45;
			npc.squaresNeeded = 2;
			break;

		case 3033:
			npc.name = "Chaos elemental";
			npc.actions = new String[5];
			npc.actions[0] = "Pick-up";
			npc.models = new int[] {11216};
			npc.combatLevel = 305;
			npc.standAnim = 3144;
			npc.walkAnim = 3145;
			npc.sizeY = 62;
			npc.sizeXZ = 62;
			break;

		case 3034:
			npc.name = "Corporeal beast";
			npc.actions = new String[5];
			npc.actions[0] = "Pick-up";
			npc.models = new int[] {40955};
			npc.combatLevel = 785;
			npc.standAnim = 10056;
			npc.walkAnim = 10055;
			npc.sizeY = 45;
			npc.sizeXZ = 45;
			npc.squaresNeeded = 2;
			break;

		case 3035:
			npc.name = "Kree'arra";
			npc.actions = new String[5];
			npc.actions[0] = "Pick-up";
			npc.models = new int[] {28003, 28004};
			npc.combatLevel = 580;
			npc.standAnim = 6972;
			npc.walkAnim = 6973;
			npc.sizeY = 43;
			npc.sizeXZ = 43;
			npc.squaresNeeded = 2;
			break;

		case 3036:
			npc.name = "K'ril tsutsaroth";
			npc.actions = new String[5];
			npc.actions[0] = "Pick-up";
			npc.models = new int[] {27768, 27773, 27764, 27765, 27770};
			npc.combatLevel = 650;
			npc.standAnim = 6943;
			npc.walkAnim = 6942;
			npc.sizeY = 43;
			npc.sizeXZ = 43;
			npc.squaresNeeded = 2;
			break;
		case 3037:
			npc.name = "Commander zilyana";
			npc.actions = new String[5];
			npc.actions[0] = "Pick-up";
			npc.models = new int[] {28057, 28071, 28078, 28056};
			npc.combatLevel = 596;
			npc.standAnim = 6963;
			npc.walkAnim = 6962;
			npc.sizeY = 103;
			npc.sizeXZ = 103;
			npc.squaresNeeded = 2;
			break;
		case 3038:
			npc.name = "Dagannoth supreme";
			npc.actions = new String[5];
			npc.actions[0] = "Pick-up";
			npc.models = new int[] {9941, 9943};
			npc.combatLevel = 303;
			npc.standAnim = 2850;
			npc.walkAnim = 2849;
			npc.sizeY = 105;
			npc.sizeXZ = 105;
			npc.squaresNeeded = 2;
			break;

		case 3039:
			npc.name = "Dagannoth prime"; //9940, 9943, 9942
			npc.actions = new String[5];
			npc.actions[0] = "Pick-up";
			npc.models = new int[] {9940, 9943, 9942};
			npc.originalColours = new int[]{11930, 27144, 16536, 16540};
			npc.destColours = new int[]{5931, 1688, 21530, 21534};
			npc.combatLevel = 303;
			npc.standAnim = 2850;
			npc.walkAnim = 2849;
			npc.sizeY = 105;
			npc.sizeXZ = 105;
			npc.squaresNeeded = 2;
			break;

		case 3040:
			npc.name = "Dagannoth rex";
			npc.actions = new String[5];
			npc.actions[0] = "Pick-up";
			npc.models = new int[] {9941};
			npc.originalColours = new int[]{16536, 16540, 27144, 2477};
			npc.destColours = new int[]{7322, 7326, 10403, 2595};
			npc.combatLevel = 303;
			npc.standAnim = 2850;
			npc.walkAnim = 2849;
			npc.sizeY = 105;
			npc.sizeXZ = 105;
			npc.squaresNeeded = 2;
			break;
		case 3047:
			NPCDef npcDef2 = NPCDef.forID(51);
			npc.models = npcDef2.models;
			npc.name = npcDef2.name;
			npc.combatLevel = npcDef2.combatLevel;
			npc.standAnim = npcDef2.standAnim;
			npc.walkAnim = npcDef2.walkAnim;
			npc.turn180AnimIndex = npcDef2.turn180AnimIndex;
			npc.turn90CCWAnimIndex = npcDef2.turn90CCWAnimIndex;
			npc.turn90CWAnimIndex = npcDef2.turn90CWAnimIndex;
			npc.type = npcDef2.type;
			npc.degreesToTurn = npcDef2.degreesToTurn;
			npc.models = new int[] {56767, 55294};
			npc.actions = new String[5];
			npc.actions[0] = "Pick-up";
			npc.sizeY = 72;
			npc.sizeXZ = 72;
			npc.squaresNeeded = 2;
			break;

		case 3048:
			NPCDef npcDef3 = NPCDef.forID(8349);
			npc.models = npcDef3.models;
			npc.name = npcDef3.name;
			npc.combatLevel = npcDef3.combatLevel;
			npc.standAnim = npcDef3.standAnim;
			npc.walkAnim = npcDef3.walkAnim;
			npc.turn180AnimIndex = npcDef3.turn180AnimIndex;
			npc.turn90CCWAnimIndex = npcDef3.turn90CCWAnimIndex;
			npc.turn90CWAnimIndex = npcDef3.turn90CWAnimIndex;
			npc.type = npcDef3.type;
			npc.degreesToTurn = npcDef3.degreesToTurn;
			npc.actions = new String[5];
			npc.actions[0] = "Pick-up";
			npc.sizeY = 60;
			npc.sizeXZ = 60;
			npc.squaresNeeded = 2;
			break;
		case 3050:
			npc.models = new int[] {24602, 24605, 24606};
			npc.name = "Kalphite queen";
			npc.actions = new String[5];
			npc.actions[0] = "Pick-up";
			npc.combatLevel = 333;
			npc.standAnim = 6236;
			npc.walkAnim = 6236;
			npc.sizeY = 70;
			npc.sizeXZ = 70;
			npc.squaresNeeded = 2;
			break;
		case 3051:
			npc.models = new int[] {46141};
			npc.name = "Slash bash";
			npc.actions = new String[5];
			npc.actions[0] = "Pick-up";
			npc.combatLevel = 111;
			npc.standAnim = 11460;
			npc.walkAnim = 11461;
			npc.sizeY = 65;
			npc.sizeXZ = 65;
			npc.squaresNeeded = 2;
			break;
		case 3052:
			npc.models = new int[] {45412};
			npc.name = "Phoenix";
			npc.actions = new String[5];
			npc.actions[0] = "Pick-up";
			npc.combatLevel = 235;
			npc.standAnim = 11074;
			npc.walkAnim = 11075;
			npc.sizeY = 70;
			npc.sizeXZ = 70;
			npc.squaresNeeded = 2;
			break;
		case 3053:
			npc.models = new int[] {46058, 46057};
			npc.name = "Bandos avatar";
			npc.actions = new String[5];
			npc.actions[0] = "Pick-up";
			npc.combatLevel = 299;
			npc.standAnim = 11242;
			npc.walkAnim = 11255;
			npc.sizeY = 70;
			npc.sizeXZ = 70;
			npc.squaresNeeded = 2;
			break;
		case 3054:
			npc.models = new int[] {62717};
			npc.name = "Nex";
			npc.actions = new String[5];
			npc.actions[0] = "Pick-up";
			npc.combatLevel = 565;
			npc.standAnim = 6320;
			npc.walkAnim = 6319;
			npc.sizeY = 95;
			npc.sizeXZ = 95;
			npc.squaresNeeded = 1;
			break;
		case 3055:
			npc.models = new int[] {51852, 51853};
			npc.name = "Jungle strykewyrm";
			npc.actions = new String[5];
			npc.actions[0] = "Pick-up";
			npc.combatLevel = 110;
			npc.standAnim = 12790;
			npc.walkAnim = 12790;
			npc.sizeY = 60;
			npc.sizeXZ = 60;
			npc.squaresNeeded = 1;
			break;
		case 3056:
			npc.models = new int[] {51848, 51850};
			npc.name = "Desert strykewyrm";
			npc.actions = new String[5];
			npc.actions[0] = "Pick-up";
			npc.combatLevel = 130;
			npc.standAnim = 12790;
			npc.walkAnim = 12790;
			npc.sizeY = 60;
			npc.sizeXZ = 60;
			npc.squaresNeeded = 1;
			break;
		case 3057:
			npc.models = new int[] {51847, 51849};
			npc.name = "Ice strykewyrm";
			npc.actions = new String[5];
			npc.actions[0] = "Pick-up";
			npc.combatLevel = 210;
			npc.standAnim = 12790;
			npc.walkAnim = 12790;
			npc.sizeY = 65;
			npc.sizeXZ = 65;
			npc.squaresNeeded = 1;
			break;
		case 3058:
			npc.models = new int[] {49142, 49144};
			npc.name = "Green dragon";
			npc.actions = new String[5];
			npc.actions[0] = "Pick-up";
			npc.combatLevel = 79;
			npc.standAnim = 12248;
			npc.walkAnim = 12246;
			npc.sizeY = 70;
			npc.sizeXZ = 70;
			npc.squaresNeeded = 2;
			break;
		case 3059:
			npc.models = new int[] {57937};
			npc.name = "Baby blue dragon";
			npc.actions = new String[5];
			npc.actions[0] = "Pick-up";
			npc.combatLevel = 48;
			npc.standAnim = 14267;
			npc.walkAnim = 14268;
			npc.sizeY = 85;
			npc.sizeXZ = 85;
			npc.squaresNeeded = 1;
			break;
		case 3060:
			npc.models = new int[] {49137, 49144};
			npc.name = "Blue dragon";
			npc.actions = new String[5];
			npc.actions[0] = "Pick-up";
			npc.combatLevel = 111;
			npc.standAnim = 12248;
			npc.walkAnim = 12246;
			npc.sizeY = 70;
			npc.sizeXZ = 70;
			npc.squaresNeeded = 2;
			break;
		case 3061:
			npc.models = new int[] {14294, 49144};
			npc.name = "Black dragon";
			npc.actions = new String[5];
			npc.actions[0] = "Pick-up";
			npc.combatLevel = 227;
			npc.standAnim = 12248;
			npc.walkAnim = 12246;
			npc.sizeY = 70;
			npc.sizeXZ = 70;
			npc.squaresNeeded = 2;
			break;
		}
		return npc;
	}

	public Model getHeadModel() {
		if (childrenIDs != null) {
			NPCDef altered = getAlteredNPCDef();
			if (altered == null)
				return null;
			else
				return altered.getHeadModel();
		}
		if (npcHeadModels == null)
			return null;
		boolean everyFetched = false;
		for (int i = 0; i < npcHeadModels.length; i++)
			if (!Model.modelIsFetched(npcHeadModels[i]))
				everyFetched = true;
		if (everyFetched)
			return null;
		Model parts[] = new Model[npcHeadModels.length];
		for (int j = 0; j < npcHeadModels.length; j++)
			parts[j] = Model.fetchModel(npcHeadModels[j]);
		Model completeModel;
		if (parts.length == 1)
			completeModel = parts[0];
		else
			completeModel = new Model(parts.length, parts);
		if (originalColours != null) {
			for (int k = 0; k < originalColours.length; k++)
				completeModel.recolour(originalColours[k], destColours[k]);
		}
		return completeModel;
	}

	public NPCDef getAlteredNPCDef() {
		try {
			int j = -1;
			if (varbitId != -1) {
				VarBit varBit = VarBit.cache[varbitId];
				int k = varBit.configId;
				int l = varBit.leastSignificantBit;
				int i1 = varBit.mostSignificantBit;
				int j1 = Client.anIntArray1232[i1 - l];
				j = clientInstance.variousSettings[k] >> l & j1;
			} else if (varSettingsId != -1) {
				j = clientInstance.variousSettings[varSettingsId];
			}
			if (j < 0 || j >= childrenIDs.length || childrenIDs[j] == -1) {
				return null;
			} else {
				return forID(childrenIDs[j]);
			}
		} catch (Exception e) {
			return null;
		}
	}

	public static int NPCAMOUNT = 11599;

	public static void unpackConfig(CacheArchive streamLoader) {
		stream = new Stream(streamLoader.getDataForName("npc.dat"));
		Stream stream2 = new Stream(streamLoader.getDataForName("npc.idx"));
		int totalNPCs = stream2.readUnsignedWord();
		streamIndices = new int[totalNPCs];
		int i = 2;
		for (int j = 0; j < totalNPCs; j++) {
			streamIndices[j] = i;
			i += stream2.readUnsignedWord();
		}
		cache = new NPCDef[20];
		for (int k = 0; k < 20; k++)
			cache[k] = new NPCDef();
		//NPCDefThing2.initialize();
	}

	public static void nullLoader() {
		modelCache = null;
		streamIndices = null;
		cache = null;
		stream = null;
	}

	public Model getAnimatedModel(int j, int k, int ai[]) {
		if (childrenIDs != null) {
			NPCDef npc = getAlteredNPCDef();
			if (npc == null)
				return null;
			else
				return npc.getAnimatedModel(j, k, ai);
		}
		Model completedModel = (Model) modelCache.get(type);
		if (completedModel == null) {
			boolean everyModelFetched = false;
			for (int ptr = 0; ptr < models.length; ptr++)
				if (!Model.modelIsFetched(models[ptr]))
					everyModelFetched = true;

			if (everyModelFetched)
				return null;
			Model parts[] = new Model[models.length];
			for (int j1 = 0; j1 < models.length; j1++)
				parts[j1] = Model.fetchModel(models[j1]);
			if (parts.length == 1)
				completedModel = parts[0];
			else
				completedModel = new Model(parts.length, parts);
			if (originalColours != null) {
				for (int k1 = 0; k1 < originalColours.length; k1++)
					completedModel.recolour(originalColours[k1], destColours[k1]);
			}
			completedModel.createBones();
			completedModel.light(frontLight, backLight, rightLight, middleLight, leftLight, true);
			modelCache.put(completedModel, type);
		}
		Model animatedModel = Model.entityModelDesc;
		animatedModel.method464(completedModel, FrameReader.isNullFrame(k) & FrameReader.isNullFrame(j));
		if (k != -1 && j != -1)
			animatedModel.method471(ai, j, k);
		else if (k != -1)
			animatedModel.applyTransform(k);
		if (sizeXZ != 128 || sizeY != 128)
			animatedModel.scaleT(sizeXZ, sizeXZ, sizeY);
		animatedModel.calculateDiagonals();
		animatedModel.triangleSkin = null;
		animatedModel.vertexSkin = null;
		if (squaresNeeded == 1)
			animatedModel.rendersWithinOneTile = true;
		return animatedModel;
	}

	public Model method164(int j, int frame, int ai[], int nextFrame, int idk, int idk2) {
		if (childrenIDs != null) {
			NPCDef npc = getAlteredNPCDef();
			if (npc == null)
				return null;
			else
				return npc.method164(j, frame, ai, nextFrame, idk, idk2);
		}
		Model completedModel = (Model) modelCache.get(type);
		if (completedModel == null) {
			boolean everyModelFetched = false;
			for (int ptr = 0; ptr < models.length; ptr++)
				if (!Model.modelIsFetched(models[ptr]))
					everyModelFetched = true;

			if (everyModelFetched)
				return null;
			Model parts[] = new Model[models.length];
			for (int j1 = 0; j1 < models.length; j1++)
				parts[j1] = Model.fetchModel(models[j1]);
			if (parts.length == 1)
				completedModel = parts[0];
			else
				completedModel = new Model(parts.length, parts);
			if (originalColours != null) {
				for (int k1 = 0; k1 < originalColours.length; k1++)
					completedModel.recolour(originalColours[k1], destColours[k1]);
			}
			completedModel.createBones();
			completedModel.light(frontLight, backLight, rightLight, middleLight, leftLight, true);
			modelCache.put(completedModel, type);
		}
		Model animatedModel = Model.entityModelDesc;
		animatedModel.method464(completedModel, FrameReader.isNullFrame(frame) & FrameReader.isNullFrame(j));

		if (frame != -1 && j != -1)
			animatedModel.method471(ai, j, frame);
		else if (frame != -1 && nextFrame != -1)
			animatedModel.applyTransform(frame, nextFrame, idk, idk2);
		else if (frame != -1)
			animatedModel.applyTransform(frame);
		if (sizeXZ != 128 || sizeY != 128)
			animatedModel.scaleT(sizeXZ, sizeXZ, sizeY);
		animatedModel.calculateDiagonals();
		animatedModel.triangleSkin = null;
		animatedModel.vertexSkin = null;
		if (squaresNeeded == 1)
			animatedModel.rendersWithinOneTile = true;
		return animatedModel;
	}

	public void readValues(Stream stream) {
		do {
			int i = stream.readUnsignedByte();
			if (i == 0)
				return;
			if (i == 1) {
				int j = stream.readUnsignedByte();
				models = new int[j];
				for (int j1 = 0; j1 < j; j1++)
					models[j1] = stream.readUnsignedWord();
			} else if (i == 2)
				name = stream.readNewString();
			else if (i == 3) {
				description = stream.readNewString();
			} else if (i == 12)
				squaresNeeded = stream.readSignedByte();
			else if (i == 13)
				standAnim = stream.readUnsignedWord();
			else if (i == 14) {
				walkAnim = stream.readUnsignedWord();
				runAnim = walkAnim;
			} else if (i == 17) {
				walkAnim = stream.readUnsignedWord();
				turn180AnimIndex = stream.readUnsignedWord();
				turn90CWAnimIndex = stream.readUnsignedWord();
				turn90CCWAnimIndex = stream.readUnsignedWord();
				if (walkAnim == 65535)
					walkAnim = -1;
				if (turn180AnimIndex == 65535)
					turn180AnimIndex = -1;
				if (turn90CWAnimIndex == 65535)
					turn90CWAnimIndex = -1;
				if (turn90CCWAnimIndex == 65535)
					turn90CCWAnimIndex = -1;
			} else if (i >= 30 && i < 40) {
				if (actions == null)
					actions = new String[5];
				actions[i - 30] = stream.readNewString();
				if (actions[i - 30].equalsIgnoreCase("hidden"))
					actions[i - 30] = null;
			} else if (i == 40) {
				int k = stream.readUnsignedByte();
				destColours = new int[k];
				originalColours = new int[k];
				for (int k1 = 0; k1 < k; k1++) {
					originalColours[k1] = stream.readUnsignedWord();
					destColours[k1] = stream.readUnsignedWord();
				}
			} else if (i == 60) {
				int l = stream.readUnsignedByte();
				npcHeadModels = new int[l];
				for (int l1 = 0; l1 < l; l1++)
					npcHeadModels[l1] = stream.readUnsignedWord();
			} else if (i == 90)
				stream.readUnsignedWord();
			else if (i == 91)
				stream.readUnsignedWord();
			else if (i == 92)
				stream.readUnsignedWord();
			else if (i == 93)
				drawMinimapDot = false;
			else if (i == 95)
				combatLevel = stream.readUnsignedWord();
			else if (i == 97)
				sizeXZ = stream.readUnsignedWord();
			else if (i == 98)
				sizeY = stream.readUnsignedWord();
			else if (i == 99)
				hasRenderPriority = true;
			else if (i == 100)
				lightning = stream.readSignedByte();
			else if (i == 101)
				shadow = stream.readSignedByte() * 5;
			else if (i == 102)
				headIcon = stream.readUnsignedWord();
			else if (i == 103)
				degreesToTurn = stream.readUnsignedWord();
			else if (i == 106) {
				varbitId = stream.readUnsignedWord();
				if (varbitId == 65535)
					varbitId = -1;
				varSettingsId = stream.readUnsignedWord();
				if (varSettingsId == 65535)
					varSettingsId = -1;
				int i1 = stream.readUnsignedByte();
				childrenIDs = new int[i1 + 1];
				for (int i2 = 0; i2 <= i1; i2++) {
					childrenIDs[i2] = stream.readUnsignedWord();
					if (childrenIDs[i2] == 65535)
						childrenIDs[i2] = -1;
				}
			} else if (i == 107)
				clickable = false;
		} while (true);
	}

	public NPCDef() {
		turn90CCWAnimIndex = -1;
		varbitId = -1;
		turn180AnimIndex = -1;
		varSettingsId = -1;
		combatLevel = -1;
		walkAnim = -1;
		squaresNeeded = 1;
		headIcon = -1;
		standAnim = -1;
		type = -1L;
		degreesToTurn = 32;
		turn90CWAnimIndex = -1;
		clickable = true;
		sizeY = 128;
		drawMinimapDot = true;
		sizeXZ = 128;
		hasRenderPriority = false;
	}

	public int turn90CCWAnimIndex;
	public static int cacheIndex;
	public int varbitId;
	public int turn180AnimIndex;
	public int varSettingsId;
	public static Stream stream;
	public int combatLevel;
	public String name;
	public String actions[];
	public int walkAnim;
	public int runAnim;
	public byte squaresNeeded;
	public int[] destColours;
	public static int[] streamIndices;
	public int[] npcHeadModels;
	public int headIcon;
	public int[] originalColours;
	public int standAnim;
	public long type;
	public int degreesToTurn;
	public static NPCDef[] cache;
	public static Client clientInstance;
	public int turn90CWAnimIndex;
	public boolean clickable;
	public int lightning;
	public int sizeY;
	public boolean drawMinimapDot;
	public int childrenIDs[];
	public String description;
	public int sizeXZ;
	public int shadow;
	public boolean hasRenderPriority;
	public int[] models;
	public static MemCache modelCache = new MemCache(30);
	public int id;
}