import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ItemDef {

	private static int[] prices;
	private static List<Integer> untradeableItems = new ArrayList<Integer>();

	public static void nullLoader() {
		modelCache = null;
		spriteCache = null;
		streamIndices = null;
		cache = null;
		stream = null;
	}

	public boolean dialogueModelFetched(int j) {
		int k = maleDialogue;
		int l = maleDialogueModel;
		if (j == 1) {
			k = femaleDialogue;
			l = femaleDialogueModel;
		}
		if (k == -1)
			return true;
		boolean flag = true;
		if (!Model.modelIsFetched(k))
			flag = false;
		if (l != -1 && !Model.modelIsFetched(l))
			flag = false;
		return flag;
	}

	public Model getDialogueModel(int gender) {
		int k = maleDialogue;
		int l = maleDialogueModel;
		if (gender == 1) {
			k = femaleDialogue;
			l = femaleDialogueModel;
		}
		if (k == -1)
			return null;
		Model model = Model.fetchModel(k);
		if (l != -1) {
			Model model_1 = Model.fetchModel(l);
			Model models[] = { model, model_1 };
			model = new Model(2, models);
		}
		if (editedModelColor != null) {
			for (int i1 = 0; i1 < editedModelColor.length; i1++)
				model.recolour(editedModelColor[i1], newModelColor[i1]);
		}
		return model;
	}

	public boolean equipModelFetched(int gender) {
		int fistModel = maleEquip1;
		int secondModel = maleEquip2;
		int thirdModel = maleEquip3;
		if (gender == 1) {
			fistModel = femaleEquip1;
			secondModel = femaleEquip2;
			thirdModel = femaleEquip3;
		}
		if (fistModel == -1)
			return true;
		boolean flag = true;
		if (!Model.modelIsFetched(fistModel))
			flag = false;
		if (secondModel != -1 && !Model.modelIsFetched(secondModel))
			flag = false;
		if (thirdModel != -1 && !Model.modelIsFetched(thirdModel))
			flag = false;
		return flag;
	}

	public Model getEquipModel(int gender) {
		int j = maleEquip1;
		int k = maleEquip2;
		int l = maleEquip3;
		if (gender == 1) {
			j = femaleEquip1;
			k = femaleEquip2;
			l = femaleEquip3;
		}
		if (j == -1)
			return null;
		Model model = Model.fetchModel(j);
		if (k != -1)
			if (l != -1) {
				Model model_1 = Model.fetchModel(k);
				Model model_3 = Model.fetchModel(l);
				Model model_1s[] = { model, model_1, model_3 };
				model = new Model(3, model_1s);
			} else {
				Model model_2 = Model.fetchModel(k);
				Model models[] = { model, model_2 };
				model = new Model(2, models);
			}
		if (j == 62367)
			model.translate(68, 7, -8);
		else if (gender == 0 && maleYOffset != 0)
			model.translate(0, maleYOffset, 0);
		else if (gender == 1 && femaleYOffset != 0)
			model.translate(0, femaleYOffset, 0);
		if (editedModelColor != null) {
			for (int i1 = 0; i1 < editedModelColor.length; i1++)
				model.recolour(editedModelColor[i1], newModelColor[i1]);
		}
		return model;
	}

	public void setDefaults() {
		untradeable = false;
		modelID = 0;
		name = null;
		description = null;
		editedModelColor = null;
		newModelColor = null;
		modelZoom = 2000;
		rotationY = 0;
		rotationX = 0;
		modelOffsetX = 0;
		modelOffset1 = 0;
		modelOffsetY = 0;
		stackable = false;
		value = 0;
		membersObject = false;
		groundActions = null;
		actions = null;
		maleEquip1 = -1;
		maleEquip2 = -1;
		maleYOffset = 0;
		maleXOffset = 0;
		femaleEquip1 = -1;
		femaleEquip2 = -1;
		femaleYOffset = 0;
		maleEquip3 = -1;
		femaleEquip3 = -1;
		maleDialogue = -1;
		maleDialogueModel = -1;
		femaleDialogue = -1;
		femaleDialogueModel = -1;
		stackIDs = null;
		stackAmounts = null;
		certID = -1;
		certTemplateID = -1;
		sizeX = 128;
		sizeY = 128;
		sizeZ = 128;
		shadow = 0;
		lightness = 0;
		team = 0;
		lendID = -1;
		lentItemID = -1;
	}

	public static void unpackConfig(CacheArchive streamLoader) {
		/*
		 * stream = new Stream(FileOperations.ReadFile("./Cache/obj.dat"));
		 * Stream stream = new
		 * Stream(FileOperations.ReadFile("./Cache/obj.idx"));
		 */
		stream = new Stream(streamLoader.getDataForName("obj.dat"));
		Stream stream = new Stream(streamLoader.getDataForName("obj.idx"));
		totalItems = stream.readUnsignedWord();
		streamIndices = new int[totalItems + 1000];
		int i = 2;
		for (int j = 0; j < totalItems; j++) {
			streamIndices[j] = i;
			i += stream.readUnsignedWord();
		}
		cache = new ItemDef[10];
		for (int k = 0; k < 10; k++)
			cache[k] = new ItemDef();
		setSettings();
	}

	public static ItemDef forID(int i) {
		for (int j = 0; j < 10; j++)
			if (cache[j].id == i)
				return cache[j];
		cacheIndex = (cacheIndex + 1) % 10;
		ItemDef itemDef = cache[cacheIndex];
		if(i >= streamIndices.length)
		{
			itemDef.id = 1;
			itemDef.setDefaults();
			return itemDef;
		}
		stream.currentOffset = streamIndices[i];
		itemDef.id = i;
		itemDef.setDefaults();
		itemDef.readValues(stream);
		if (itemDef.certTemplateID != -1)
			itemDef.toNote();
		if (itemDef.lentItemID != -1)
			itemDef.toLend();
		if (itemDef.id == i && itemDef.editedModelColor == null) {
			itemDef.editedModelColor = new int [1];
			itemDef.newModelColor = new int [1];
			itemDef.editedModelColor[0] = 0;
			itemDef.newModelColor[0] = 1;
		}
		if(untradeableItems.contains(itemDef.id)) {
			itemDef.untradeable = true;
		}
		itemDef.value = prices[itemDef.id];
		switch (i) {
		case 19670:
			itemDef.name = "Vote scroll";
			itemDef.actions = new String[5];
			itemDef.actions[4] = "Drop";
			itemDef.actions[0] = "Claim";
			itemDef.actions[2] = "Claim-All";
			break;
		case 10034:
		case 10033:
			itemDef.actions = new String[] { null, null, null, null, "Drop"};
			break;
		case 13727:
			itemDef.actions = new String[] { null, null, null, null, "Drop"};
			break;
		case 6500:
			itemDef.modelID = 9123;
			itemDef.name = "Charming imp";
			//	itemDef.modelZoom = 672;
			//	itemDef.rotationY = 85;
			//	itemDef.rotationX = 1867;
			itemDef.actions = new String[] { null, null, "Check", "Config", "Drop"};
			break;
		case 11995:
			itemDef.name = "Pet Chaos elemental";
			ItemDef itemDef2 = ItemDef.forID(12458);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
			break;
		case 11996:
			itemDef.name = "Pet King black dragon";
			itemDef2 = ItemDef.forID(12458);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
			break;
		case 11997:
			itemDef.name = "Pet General graardor";
			itemDef2 = ItemDef.forID(12458);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
			break;
		case 11978:
			itemDef.name = "Pet TzTok-Jad";
			itemDef2 = ItemDef.forID(12458);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
			break;
		case 12001:
			itemDef.name = "Pet Corporeal beast";
			itemDef2 = ItemDef.forID(12458);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
			break;
		case 12002:
			itemDef.name = "Pet Kree'arra";
			itemDef2 = ItemDef.forID(12458);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
			break;
		case 12003:
			itemDef.name = "Pet K'ril tsutsaroth";
			itemDef2 = ItemDef.forID(12458);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
			break;
		case 12004:
			itemDef.name = "Pet Commander zilyana";
			itemDef2 = ItemDef.forID(12458);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
			break;
		case 12005:
			itemDef.name = "Pet Dagannoth supreme";
			itemDef2 = ItemDef.forID(12458);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
			break;
		case 12006:
			itemDef.name = "Pet Dagannoth prime";
			itemDef2 = ItemDef.forID(12458);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
			break;
		case 11990:
			itemDef.name = "Pet Dagannoth rex";
			itemDef2 = ItemDef.forID(12458);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
			break;
		case 11991:
			itemDef.name = "Pet Frost dragon";
			itemDef2 = ItemDef.forID(12458);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
			break;
		case 11992:
			itemDef.name = "Pet Tormented demon";
			itemDef2 = ItemDef.forID(12458);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
			break;
		case 11993:
			itemDef.name = "Pet Kalphite queen";
			itemDef2 = ItemDef.forID(12458);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
			break;
		case 11994:
			itemDef.name = "Pet Slash bash";
			itemDef2 = ItemDef.forID(12458);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
			break;
		case 11989:
			itemDef.name = "Pet Phoenix";
			itemDef2 = ItemDef.forID(12458);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
			break;
		case 11988:
			itemDef.name = "Pet Bandos avatar";
			itemDef2 = ItemDef.forID(12458);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
			break;
		case 11987:
			itemDef.name = "Pet Nex";
			itemDef2 = ItemDef.forID(12458);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
			break;
		case 11986:
			itemDef.name = "Pet Jungle strykewyrm";
			itemDef2 = ItemDef.forID(12458);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
			break;
		case 11985:
			itemDef.name = "Pet Desert strykewyrm";
			itemDef2 = ItemDef.forID(12458);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
			break;
		case 11984:
			itemDef.name = "Pet Ice strykewyrm";
			itemDef2 = ItemDef.forID(12458);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
			break;
		case 11983:
			itemDef.name = "Pet Green dragon";
			itemDef2 = ItemDef.forID(12458);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
			break;
		case 11982:
			itemDef.name = "Pet Baby blue dragon";
			itemDef2 = ItemDef.forID(12458);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
			break;
		case 11981:
			itemDef.name = "Pet Blue dragon";
			itemDef2 = ItemDef.forID(12458);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
			break;
		case 11979:
			itemDef.name = "Pet Black dragon";
			itemDef2 = ItemDef.forID(12458);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, null, "Summon", null, "Drop" };
			break;
		case 14667:
			itemDef.name = "Zombie fragment";
			itemDef.modelID = ItemDef.forID(14639).modelID;
			break;
		case 15182:
			itemDef.actions[0] = "Bury";
			break;
		case 15084:
			itemDef.actions[0] = "Roll";
			itemDef.name = "Dice (up to 100)";
			itemDef2 = ItemDef.forID(15098);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			break;
		case 2996:
			itemDef.name = "Agility ticket";
			break;
		case 5510:
		case 5512:
		case 5509:
			itemDef.actions = new String[]{"Fill", null, "Empty", "Check", null, null};
			break;
		case 11998:
			itemDef.name = "Scimitar";
			itemDef.actions = new String[]{null, null, null, null, null, null};
			break;
		case 11999:
			itemDef.name = "Scimitar";
			itemDef.actions = new String[]{null, null, null, null, null, null};
			itemDef.modelZoom = 700;
			itemDef.rotationX = 0;
			itemDef.rotationY = 350;
			itemDef.modelID = 2429;
			itemDef.modelOffsetX = itemDef.modelOffset1 = 0;
			itemDef.stackable = true;
			itemDef.certID = 11998;
			itemDef.certTemplateID = 799;
			break;
		case 1389:
			itemDef.name = "Staff";
			itemDef.actions = new String[]{null, null, null, null, null, null};
			break;
		case 1390:
			itemDef.name = "Staff";
			itemDef.actions = new String[]{null, null, null, null, null, null};
			break;
		case 17401:
			itemDef.name = "Damaged Hammer";
			itemDef.actions = new String[]{null, null, null, null, null, null};
			break;
		case 17402:
			itemDef.name = "Damaged Hammer";
			itemDef.actions = new String[]{null, null, null, null, null, null};
			itemDef.modelZoom = 760;
			itemDef.rotationX = 28;
			itemDef.rotationY = 552;
			itemDef.modelID = 2429;
			itemDef.modelOffsetX = itemDef.modelOffset1 = 0;
			itemDef.stackable = true;
			itemDef.certID = 17401;
			itemDef.certTemplateID = 799;
			break;
		case 15009:
			itemDef.name = "Gold Ring";
			itemDef.actions = new String[]{null, null, null, null, null, null};
			break;
		case 15010:
			itemDef.modelID = 2429;
			itemDef.name = "Gold Ring";
			itemDef.actions = new String[]{null, null, null, null, null, null};
			itemDef.modelZoom = 760;
			itemDef.rotationX = 28;
			itemDef.rotationY = 552;
			itemDef.modelOffsetX = itemDef.modelOffset1 = 0;
			itemDef.stackable = true;
			itemDef.certID = 15009;
			itemDef.certTemplateID = 799;
			break;

		case 11884:
			itemDef.actions = new String[]{"Open", null, null, null, null, null};
			break;
		case 14207:
			itemDef.name = "Potion flask";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.groundActions[2] = "Take";
			itemDef.modelID = 61741;
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			break;
		case 14200:
			itemDef.name = "Prayer flask (6)";
			itemDef.description = "6 doses of prayer potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 28488 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61732;
			break;
		case 14198:
			itemDef.name = "Prayer flask (5)";
			itemDef.description = "5 doses of prayer potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 28488 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61729;
			break;
		case 14196:
			itemDef.name = "Prayer flask (4)";
			itemDef.description = "4 doses of prayer potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 28488 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61764;
			break;
		case 14194:
			itemDef.name = "Prayer flask (3)";
			itemDef.description = "3 doses of prayer potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 28488 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61727;
			break;
		case 14192:
			itemDef.name = "Prayer flask (2)";
			itemDef.description = "2 doses of prayer potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 28488 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61731;
			break;
		case 14190:
			itemDef.name = "Prayer flask (1)";
			itemDef.description = "1 dose of prayer potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 28488 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61812;
			break;
		case 14188:
			itemDef.name = "Super attack flask (6)";
			itemDef.description = "6 doses of super attack potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 43848 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61732;
			break;
		case 14186:
			itemDef.name = "Super attack flask (5)";
			itemDef.description = "5 doses of super attack potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 43848 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61729;
			break;
		case 14184:
			itemDef.name = "Super attack flask (4)";
			itemDef.description = "4 doses of super attack potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 43848 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61764;
			break;
		case 14182:
			itemDef.name = "Super attack flask (3)";
			itemDef.description = "3 doses of super attack potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 43848 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61727;
			break;
		case 14180:
			itemDef.name = "Super attack flask (2)";
			itemDef.description = "2 doses of super attack potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 43848 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";

			itemDef.modelID = 61731;
			break;
		case 14178:
			itemDef.name = "Super attack flask (1)";
			itemDef.description = "1 dose of super attack potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 43848 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61812;
			break;
		case 14176:
			itemDef.name = "Super strength flask (6)";
			itemDef.description = "6 doses of super strength potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 119 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61732;
			break;
		case 14174:
			itemDef.name = "Super strength flask (5)";
			itemDef.description = "5 doses of super strength potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 119 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61729;
			break;
		case 14172:
			itemDef.name = "Super strength flask (4)";
			itemDef.description = "4 doses of super strength potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 119 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61764;
			break;
		case 14170:
			itemDef.name = "Super strength flask (3)";
			itemDef.description = "3 doses of super strength potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 119 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61727;
			break;
		case 14168:
			itemDef.name = "Super strength flask (2)";
			itemDef.description = "2 doses of super strength potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 119 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61731;
			break;
		case 14166:
			itemDef.name = "Super strength flask (1)";
			itemDef.description = "1 dose of super strength potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 119 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61812;
			break;
		case 14164:
			itemDef.name = "Super defence flask (6)";
			itemDef.description = "6 doses of super defence potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 8008 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61732;
			break;
		case 14162:
			itemDef.name = "Super defence flask (5)";
			itemDef.description = "5 doses of super defence potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 8008 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61729;
			break;
		case 14160:
			itemDef.name = "Super defence flask (4)";
			itemDef.description = "4 doses of super defence potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 8008 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61764;
			break;
		case 14158:
			itemDef.name = "Super defence flask (3)";
			itemDef.description = "3 doses of super defence potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 8008 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61727;
			break;
		case 14156:
			itemDef.name = "Super defence flask (2)";
			itemDef.description = "2 doses of super defence potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 8008 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61731;
			break;
		case 14154:
			itemDef.name = "Super defence flask (1)";
			itemDef.description = "1 dose of super defence potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 8008 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61812;
			break;
		case 14152:
			itemDef.name = "Ranging flask (6)";
			itemDef.description = "6 doses of ranging potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 36680 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61732;
			break;
		case 14150:
			itemDef.name = "Ranging flask (5)";
			itemDef.description = "5 doses of ranging potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 36680 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61729;
			break;
		case 14148:
			itemDef.name = "Ranging flask (4)";
			itemDef.description = "4 doses of ranging potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 36680 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";

			itemDef.modelID = 61764;
			break;
		case 14146:
			itemDef.name = "Ranging flask (3)";
			itemDef.description = "3 doses of ranging potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 36680 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61727;
			break;
		case 14144:
			itemDef.name = "Ranging flask (2)";
			itemDef.description = "2 doses of ranging potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 36680 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61731;
			break;
		case 14142:
			itemDef.name = "Ranging flask (1)";
			itemDef.description = "1 dose of ranging potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 36680 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61812;
			break;
		case 14140:
			itemDef.name = "Super antipoison flask (6)";
			itemDef.description = "6 doses of super antipoison.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 62404 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61732;
			break;
		case 14138:
			itemDef.name = "Super antipoison flask (5)";
			itemDef.description = "5 doses of super antipoison.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 62404 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61729;
			break;
		case 14136:
			itemDef.name = "Super antipoison flask (4)";
			itemDef.description = "4 doses of super antipoison.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 62404 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61764;
			break;
		case 14134:
			itemDef.name = "Super antipoison flask (3)";
			itemDef.description = "3 doses of super antipoison.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 62404 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61727;
			break;
		case 14132:
			itemDef.name = "Super antipoison flask (2)";
			itemDef.description = "2 doses of super antipoison.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 62404 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61731;
			break;
		case 14130:
			itemDef.name = "Super antipoison flask (1)";
			itemDef.description = "1 dose of super antipoison.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 62404 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61812;
			break;
		case 14128:
			itemDef.name = "Saradomin brew flask (6)";
			itemDef.description = "6 doses of saradomin brew.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 10939 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61732;
			/*	itemDef.anInt196 = 40;
			itemDef.anInt184 = 200;*/
			break;
		case 14126:
			itemDef.name = "Saradomin brew flask (5)";
			itemDef.description = "5 doses of saradomin brew.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 10939 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61729;
			/*	itemDef.anInt196 = 40;
			itemDef.anInt184 = 200;*/
			break;
		case 14124:
			itemDef.name = "Saradomin brew flask (4)";
			itemDef.description = "4 doses of saradomin brew.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 10939 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61764;
			/*	itemDef.anInt196 = 40;
			itemDef.anInt184 = 200;*/
			break;
		case 14122:
			itemDef.name = "Saradomin brew flask (3)";
			itemDef.description = "3 doses of saradomin brew.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 10939 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61727;
			/*	itemDef.anInt196 = 40;
			itemDef.anInt184 = 200;*/
			break;
		case 14419:
			itemDef.name = "Saradomin brew flask (2)";
			itemDef.description = "2 doses of saradomin brew.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 10939 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61731;
			/*	itemDef.anInt196 = 40;
			itemDef.anInt184 = 200;*/
			break;
		case 14417:
			itemDef.name = "Saradomin brew flask (1)";
			itemDef.description = "1 dose of saradomin brew.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 10939 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61812;
			/*	itemDef.anInt196 = 40;
			itemDef.anInt184 = 200;*/
			break;
		case 14415:
			itemDef.name = "Super restore flask (6)";
			itemDef.description = "6 doses of super restore potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 62135 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61732;
			break;
		case 14413:
			itemDef.name = "Super restore flask (5)";
			itemDef.description = "5 doses of super restore potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 62135 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61729;
			break;
		case 14411:
			itemDef.name = "Super restore flask (4)";
			itemDef.description = "4 doses of super restore potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 62135 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61764;
			break;
		case 14409:
			itemDef.name = "Super restore flask (3)";
			itemDef.description = "3 doses of super restore potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 62135 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61727;
			break;
		case 14407:
			itemDef.name = "Super restore flask (2)";
			itemDef.description = "2 doses of super restore potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 62135 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61731;
			break;
		case 14405:
			itemDef.name = "Super restore flask (1)";
			itemDef.description = "1 dose of super restore potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 62135 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61812;
			break;
		case 14403:
			itemDef.name = "Magic flask (6)";
			itemDef.description = "6 doses of magic potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 37440 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61732;
			break;
		case 14401:
			itemDef.name = "Magic flask (5)";
			itemDef.description = "5 doses of magic potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 37440 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61729;
			break;
		case 14399:
			itemDef.name = "Magic flask (4)";
			itemDef.description = "4 doses of magic potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 37440 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61764;
			break;
		case 14397:
			itemDef.name = "Magic flask (3)";
			itemDef.description = "3 doses of magic potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 37440 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61727;
			break;
		case 14395:
			itemDef.name = "Magic flask (2)";
			itemDef.description = "2 doses of magic potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 37440 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61731;
			break;
		case 14393:
			itemDef.name = "Magic flask (1)";
			itemDef.description = "1 dose of magic potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 37440 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61812;
			break;
		case 14385:
			itemDef.name = "Recover special flask (6)";
			itemDef.description = "6 doses of recover special.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 38222 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61732;
			break;
		case 14383:
			itemDef.name = "Recover special flask (5)";
			itemDef.description = "5 doses of recover special.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 38222 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61729;
			break;
		case 14381:
			itemDef.name = "Recover special flask (4)";
			itemDef.description = "4 doses of recover special.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 38222 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61764;
			break;
		case 14379:
			itemDef.name = "Recover special flask (3)";
			itemDef.description = "3 doses of recover special.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 38222 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61727;
			break;
		case 14377:
			itemDef.name = "Recover special flask (2)";
			itemDef.description = "2 doses of recover special.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 38222 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61731;
			break;
		case 14375:
			itemDef.name = "Recover special flask (1)";
			itemDef.description = "1 dose of recover special.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 38222 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61812;
			break;
		case 14373:
			itemDef.name = "Extreme attack flask (6)";
			itemDef.description = "6 doses of extreme attack potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 33112 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61732;
			break;
		case 14371:
			itemDef.name = "Extreme attack flask (5)";
			itemDef.description = "5 doses of extreme attack potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 33112 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61729;
			break;
		case 14369:
			itemDef.name = "Extreme attack flask (4)";
			itemDef.description = "4 doses of extreme attack potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 33112 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61764;
			break;
		case 14367:
			itemDef.name = "Extreme attack flask (3)";
			itemDef.description = "3 doses of extreme attack potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 33112 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61727;
			break;
		case 14365:
			itemDef.name = "Extreme attack flask (2)";
			itemDef.description = "2 doses of extreme attack potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 33112 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61731;
			break;
		case 14363:
			itemDef.name = "Extreme attack flask (1)";
			itemDef.description = "1 dose of extreme attack potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 33112 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61812;
			break;
		case 14361:
			itemDef.name = "Extreme strength flask (6)";
			itemDef.description = "6 doses of extreme strength potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 127 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61732;
			break;
		case 14359:
			itemDef.name = "Extreme strength flask (5)";
			itemDef.description = "5 doses of extreme strength potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 127 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61729;
			break;
		case 14357:
			itemDef.name = "Extreme strength flask (4)";
			itemDef.description = "4 doses of extreme strength potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 127 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61764;
			break;
		case 14355:
			itemDef.name = "Extreme strength flask (3)";
			itemDef.description = "3 doses of extreme strength potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 127 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61727;
			break;
		case 14353:
			itemDef.name = "Extreme strength flask (2)";
			itemDef.description = "2 doses of extreme strength potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 127 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61731;
			break;
		case 14351:
			itemDef.name = "Extreme strength flask (1)";
			itemDef.description = "1 dose of extreme strength potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 127 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61812;
			break;
		case 14349:
			itemDef.name = "Extreme defence flask (6)";
			itemDef.description = "6 doses of extreme defence potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 10198 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61732;
			break;
		case 14347:
			itemDef.name = "Extreme defence flask (5)";
			itemDef.description = "5 doses of extreme defence potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 10198 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61729;
			break;
		case 14345:
			itemDef.name = "Extreme defence flask (4)";
			itemDef.description = "4 doses of extreme defence potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 10198 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61764;
			break;
		case 14343:
			itemDef.name = "Extreme defence flask (3)";
			itemDef.description = "3 doses of extreme defence potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 10198 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61727;
			break;
		case 14341:
			itemDef.name = "Extreme defence flask (2)";
			itemDef.description = "2 doses of extreme defence potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 10198 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61731;
			break;
		case 14339:
			itemDef.name = "Extreme defence flask (1)";
			itemDef.description = "1 dose of extreme defence potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 10198 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61812;
			break;
		case 14337:
			itemDef.name = "Extreme magic flask (6)";
			itemDef.description = "6 doses of extreme magic potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 33490 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61732;
			break;
		case 14335:
			itemDef.name = "Extreme magic flask (5)";
			itemDef.description = "5 doses of extreme magic potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 33490 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61729;
			break;
		case 14333:
			itemDef.name = "Extreme magic flask (4)";
			itemDef.description = "4 doses of extreme magic potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 33490 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61764;
			break;
		case 14331:
			itemDef.name = "Extreme magic flask (3)";
			itemDef.description = "3 doses of extreme magic potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 33490 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61727;
			break;
		case 14329:
			itemDef.name = "Extreme magic flask (2)";
			itemDef.description = "2 doses of extreme magic potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 33490 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61731;
			break;
		case 14327:
			itemDef.name = "Extreme magic flask (1)";
			itemDef.description = "1 dose of extreme magic potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 33490 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61812;
			break;
		case 14325:
			itemDef.name = "Extreme ranging flask (6)";
			itemDef.description = "6 doses of extreme ranging potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 13111 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61732;
			break;
		case 14323:
			itemDef.name = "Extreme ranging flask (5)";
			itemDef.description = "5 doses of extreme ranging potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 13111 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61729;
			break;
		case 14321:
			itemDef.name = "Extreme ranging flask (4)";
			itemDef.description = "4 doses of extreme ranging potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 13111 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61764;
			break;
		case 14319:
			itemDef.name = "Extreme ranging flask (3)";
			itemDef.description = " 3 doses of extreme ranging potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 13111 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61727;
			break;
		case 14317:
			itemDef.name = "Extreme ranging flask (2)";
			itemDef.description = "2 doses of extreme ranging potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 13111 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61731;
			break;
		case 14315:
			itemDef.name = "Extreme ranging flask (1)";
			itemDef.description = "1 dose of extreme ranging potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 13111 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61812;
			break;
		case 14313:
			itemDef.name = "Super prayer flask (6)";
			itemDef.description = "6 doses of super prayer potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 3016 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61732;
			break;
		case 14311:
			itemDef.name = "Super prayer flask (5)";
			itemDef.description = "5 doses of super prayer potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 3016 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61729;
			break;
		case 14309:
			itemDef.name = "Super prayer flask (4)";
			itemDef.description = "4 doses of super prayer potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 3016 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61764;
			break;
		case 14307:
			itemDef.name = "Super prayer flask (3)";
			itemDef.description = "3 doses of super prayer potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 3016 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61727;
			break;
		case 14305:
			itemDef.name = "Super prayer flask (2)";
			itemDef.description = "2 doses of super prayer potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 3016 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61731;
			break;
		case 14303:
			itemDef.name = "Super prayer flask (1)";
			itemDef.description = "1 dose of super prayer potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 3016 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61812;
			break;
		case 14301:
			itemDef.name = "Overload flask (6)";
			itemDef.description = "6 doses of overload potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 0 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61732;
			break;
		case 14299:
			itemDef.name = "Overload flask (5)";
			itemDef.description = "5 doses of overload potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 0 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61729;
			break;
		case 14297:
			itemDef.name = "Overload flask (4)";
			itemDef.description = "4 doses of overload potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 0 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61764;
			break;
		case 14295:
			itemDef.name = "Overload flask (3)";
			itemDef.description = "3 doses of overload potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 0 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61727;
			break;
		case 14293:
			itemDef.name = "Overload flask (2)";
			itemDef.description = "2 doses of overload potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 0 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61731;
			break;
		case 14291:
			itemDef.name = "Overload flask (1)";
			itemDef.description = "1 dose of overload potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 0 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.groundActions[2] = "Take";

			itemDef.modelID = 61812;
			break;
		case 14289:
			itemDef.name = "Prayer renewal flask (6)";
			itemDef.description = "6 doses of prayer renewal.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 926 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61732;
			break;
		case 14287:
			itemDef.name = "Prayer renewal flask (5)";
			itemDef.description = "5 doses of prayer renewal.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 926 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61729;
			break;
		case 15123:
			itemDef.name = "Prayer renewal flask (4)";
			itemDef.description = "4 doses of prayer renewal potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 926 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61764;
			break;
		case 15121:
			itemDef.name = "Prayer renewal flask (3)";
			itemDef.description = "3 doses of prayer renewal potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 926 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61727;
			break;
		case 15119:
			itemDef.name = "Prayer renewal flask (2)";
			itemDef.description = "2 doses of prayer renewal potion.";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 926 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61731;
			break;
		case 7340:
			itemDef.name = "Prayer renewal flask (1)";
			itemDef.description = "1 dose of prayer renewal potion";
			itemDef.modelZoom = 804;
			itemDef.rotationY = 131;
			itemDef.rotationX = 198;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffset1 = -1;
			itemDef.newModelColor = new int[] { 926 };
			itemDef.editedModelColor  = new int[] { 33715 };
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[]{"Drink", null, null, null, null, null};
			itemDef.modelID = 61812;
			break;
		case 15262:
			itemDef.actions = new String[5];
			itemDef.actions[0] = "Open";
			itemDef.actions[2] = "Open-All";
			break;
		case 6570:
			itemDef.actions[2] = "Upgrade";
			break;
		case 4155:
			itemDef.name = "Slayer gem";
			itemDef.actions = new String[] {"Activate", null, "Social-Slayer", null, "Destroy"};
			break;
		case 13663:
			itemDef.name = "Stat reset cert.";
			itemDef.actions = new String[5];
			itemDef.actions[4] = "Drop";
			itemDef.actions[0] = "Open";
			break;
		case 13653:
			itemDef.name = "Energy fragment";
			break;
		case 292:
			itemDef.name = "Ingredients book";
			break;
		case 15707:
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[0] = "Create Party";
			break;
		case 14501:
			itemDef.modelID = 44574;
			itemDef.maleEquip1 = 43693;
			itemDef.femaleEquip1= 43693;
			break;
		case 19111:
			itemDef.name ="TokHaar-Kal";
			itemDef.value = 60000;
			itemDef.maleEquip1 = 62575;
			itemDef.maleEquip1 = 62582;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.modelOffset1 = -4;
			itemDef.modelID = 62592;
			itemDef.stackable = false;
			itemDef.description = "A cape made of ancient, enchanted rocks.";
			itemDef.modelZoom = 2086;
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			itemDef.modelOffsetX = 0;
			itemDef.rotationY = 533;
			itemDef.rotationX = 333;
			break;
		case 13262:
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.editedModelColor = new int[1];
			itemDef.newModelColor = new int[1];
			itemDef.editedModelColor[0] = 28; // colors
			itemDef.editedModelColor[0] = 74; // colors
			itemDef.newModelColor[0] = 38676; // colors
			itemDef.newModelColor[0] = 924; // colors 
			itemDef.modelID = 15335;//inventory/drop model
			itemDef.maleEquip1 = 15413;
			itemDef.femaleEquip1 = 15413;
			itemDef.modelZoom = 490;//Model Zoom
			itemDef.rotationY = 344;//Model Rotation1
			itemDef.rotationX = 192;//Model Rotation2
			itemDef.modelOffsetY = 1;//Model Offset 1
			itemDef.modelOffsetX = 20;// model Offset 2
			itemDef.name = "Dragon Defender";
			itemDef.description = "A pointy off-hand knife.";
			break;
		case 10942:
			itemDef.name = "$10 Scroll";
			itemDef.actions = new String[5];
			itemDef.actions[4] = "Drop";
			itemDef.actions[0] = "Claim";
			itemDef2 = ItemDef.forID(761);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			break;
		case 10934:
			itemDef.name = "$20 Scroll";
			itemDef.actions = new String[5];
			itemDef.actions[4] = "Drop";
			itemDef.actions[0] = "Claim";
			itemDef2 = ItemDef.forID(761);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			break;
		case 10935:
			itemDef.name = "$50 Scroll";
			itemDef.actions = new String[5];
			itemDef.actions[4] = "Drop";
			itemDef.actions[0] = "Claim";
			itemDef2 = ItemDef.forID(761);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			break;
		case 10943:
			itemDef.name = "$100 Scroll";
			itemDef.actions = new String[5];
			itemDef.actions[4] = "Drop";
			itemDef.actions[0] = "Claim";
			itemDef2 = ItemDef.forID(761);
			itemDef.modelID = itemDef2.modelID;
			itemDef.modelOffset1 = itemDef2.modelOffset1;
			itemDef.modelOffsetX = itemDef2.modelOffsetX;
			itemDef.modelOffsetY = itemDef2.modelOffsetY;
			itemDef.modelZoom = itemDef2.modelZoom;
			break;
		case 995:
			itemDef.name = "Coins";
			itemDef.actions = new String[5];
			itemDef.actions[4] = "Drop";
			itemDef.actions[3] = "Add-to-pouch";
			break;
		case 17291:
			itemDef.name = "Blood necklace";
			itemDef.actions = new String[] {null, "Wear", null, null, null, null};
			break;
		case 20084:
			itemDef.name = "Golden Maul";
			break;
		case 6199:
			itemDef.name = "Mystery Box";
			itemDef.actions = new String[5];
			itemDef.actions[0] = "Open";
			break;
		case 15501:
			itemDef.name = "Legendary Mystery Box";
			itemDef.actions = new String[5];
			itemDef.actions[0] = "Open";
			break;
		case 6568: // To replace Transparent black with opaque black.
			itemDef.editedModelColor = new int[1];
			itemDef.newModelColor = new int[1];
			itemDef.editedModelColor[0] = 0;
			itemDef.newModelColor[0] = 2059;
			break;
		case 996:
		case 997:
		case 998:
		case 999:
		case 1000:
		case 1001:
		case 1002:
		case 1003:
		case 1004:
			itemDef.name = "Coins";
			break;

		case 14017:
			itemDef.name = "Brackish blade";
			itemDef.modelZoom = 1488;
			itemDef.rotationY = 276;
			itemDef.rotationX = 1580;
			itemDef.modelOffsetY = 1;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, "Wield", null, null, "Drop" };
			itemDef.modelID = 64593;
			itemDef.maleEquip1 = 64704;
			itemDef.femaleEquip2 = 64704;
			break;

		case 15220:
			itemDef.name = "Berserker ring (i)";
			itemDef.modelZoom = 600;
			itemDef.rotationY = 324;
			itemDef.rotationX = 1916;
			itemDef.modelOffset1 = 3;
			itemDef.modelOffsetY = -15;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			itemDef.modelID = 7735; // if it doesn't work try 7735
			itemDef.maleEquip1 = -1;
			// itemDefinition.maleArm = -1;
			itemDef.femaleEquip1 = -1;
			// itemDefinition.femaleArm = -1;
			break;
		case 14019:
			itemDef.modelID = 65262;
			itemDef.name = "Max Cape";
			itemDef.description = "A cape worn by those who've achieved 99 in all skills.";
			itemDef.modelZoom = 1385;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffsetY = 24;
			itemDef.rotationY = 279;
			itemDef.rotationX = 948;
			itemDef.maleEquip1 = 65300;
			itemDef.femaleEquip1 = 65322;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			break;
		case 14020:
			itemDef.name = "Veteran hood";
			itemDef.description = "A hood worn by Chivalry's veterans.";
			itemDef.modelZoom = 760;
			itemDef.rotationY = 11;
			itemDef.rotationX = 81;
			itemDef.modelOffset1 = 1;
			itemDef.modelOffsetY = -3;
			itemDef.groundActions = new String[] { null, null, "Take", null, null };
			itemDef.actions = new String[] { null, "Wear", null, null, "Drop" };
			itemDef.modelID = 65271;
			itemDef.maleEquip1 = 65289;
			itemDef.femaleEquip1 = 65314;
			break;
		case 14021:
			itemDef.modelID = 65261;
			itemDef.name = "Veteran Cape";
			itemDef.description = "A cape worn by Chivalry's veterans.";
			itemDef.modelZoom = 760;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffsetY = 24;
			itemDef.rotationY = 279;
			itemDef.rotationX = 948;
			itemDef.maleEquip1 = 65305;
			itemDef.femaleEquip1 = 65318;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			break;
		case 14022:
			itemDef.modelID = 65270;
			itemDef.name = "Completionist Cape";
			itemDef.description = "We'd pat you on the back, but this cape would get in the way.";
			itemDef.modelZoom = 1385;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffsetY = 24;
			itemDef.rotationY = 279;
			itemDef.rotationX = 948;
			itemDef.maleEquip1 = 65297;
			itemDef.femaleEquip1 = 65297;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			break;
		case 9666:
		case 11814:
		case 11816:
		case 11818:
		case 11820:
		case 11822:
		case 11824:
		case 11826:
		case 11828:
		case 11830:
		case 11832:
		case 11834:
		case 11836:
		case 11838:
		case 11840:
		case 11842:
		case 11844:
		case 11846:
		case 11848:
		case 11850:
		case 11852:
		case 11854:
		case 11856:
		case 11858:
		case 11860:
		case 11862:
		case 11864:
		case 11866:
		case 11868:
		case 11870:
		case 11874:
		case 11876:
		case 11878:
		case 11882:
		case 11886:
		case 11890:
		case 11894:
		case 11898:
		case 11902:
		case 11904:
		case 11906:
		case 11926:
		case 11928:
		case 11930:
		case 11938:
		case 11942:
		case 11944:
		case 11946:
		case 14525:
		case 14527:
		case 14529:
		case 14531:
		case 19588:
		case 19592:
		case 19596:
		case 11908:
		case 11910:
		case 11912:
		case 11914:
		case 11916:
		case 11618:
		case 11920:
		case 11922:
		case 11924:
		case 11960:
		case 11962:
		case 11967:
		case 19586:
		case 19584:
		case 19590:
		case 19594:
		case 19598:
			itemDef.actions = new String[5];
			itemDef.actions[0] = "Open";
			break;

		case 14004:
			itemDef.name = "Staff of light";
			itemDef.modelID = 51845;
			itemDef.editedModelColor = new int [11];
			itemDef.newModelColor = new int [11];
			itemDef.editedModelColor[0] = 7860;
			itemDef.newModelColor[0] = 38310;
			itemDef.editedModelColor[1] = 7876;
			itemDef.newModelColor[1] = 38310;
			itemDef.editedModelColor[2] = 7892;
			itemDef.newModelColor[2] = 38310;
			itemDef.editedModelColor[3] = 7884;
			itemDef.newModelColor[3] = 38310;
			itemDef.editedModelColor[4] = 7868;
			itemDef.newModelColor[4] = 38310;
			itemDef.editedModelColor[5] = 7864;
			itemDef.newModelColor[5] = 38310;
			itemDef.editedModelColor[6] = 7880;
			itemDef.newModelColor[6] = 38310;
			itemDef.editedModelColor[7] = 7848;
			itemDef.newModelColor[7] = 38310;
			itemDef.editedModelColor[8] = 7888;
			itemDef.newModelColor[8] = 38310;
			itemDef.editedModelColor[9] = 7872;
			itemDef.newModelColor[9] = 38310;
			itemDef.editedModelColor[10] = 7856;
			itemDef.newModelColor[10] = 38310;
			itemDef.modelZoom = 2256;
			itemDef.rotationX = 456;
			itemDef.rotationY = 513;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffset1 = 0;
			itemDef.maleEquip1 = 51795;
			itemDef.femaleEquip1 = 51795;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			break;

		case 14005:
			itemDef.name = "Staff of light";
			itemDef.modelID = 51845;
			itemDef.editedModelColor = new int [11];
			itemDef.newModelColor = new int [11];
			itemDef.editedModelColor[0] = 7860;
			itemDef.newModelColor[0] = 432;
			itemDef.editedModelColor[1] = 7876;
			itemDef.newModelColor[1] = 432;
			itemDef.editedModelColor[2] = 7892;
			itemDef.newModelColor[2] = 432;
			itemDef.editedModelColor[3] = 7884;
			itemDef.newModelColor[3] = 432;
			itemDef.editedModelColor[4] = 7868;
			itemDef.newModelColor[4] = 432;
			itemDef.editedModelColor[5] = 7864;
			itemDef.newModelColor[5] = 432;
			itemDef.editedModelColor[6] = 7880;
			itemDef.newModelColor[6] = 432;
			itemDef.editedModelColor[7] = 7848;
			itemDef.newModelColor[7] = 432;
			itemDef.editedModelColor[8] = 7888;
			itemDef.newModelColor[8] = 432;
			itemDef.editedModelColor[9] = 7872;
			itemDef.newModelColor[9] = 432;
			itemDef.editedModelColor[10] = 7856;
			itemDef.newModelColor[10] = 432;
			itemDef.modelZoom = 2256;
			itemDef.rotationX = 456;
			itemDef.rotationY = 513;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffset1 = 0;
			itemDef.maleEquip1 = 51795;
			itemDef.femaleEquip1 = 51795;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			break;

		case 14006:
			itemDef.name = "Staff of light";
			itemDef.modelID = 51845;
			itemDef.editedModelColor = new int [11];
			itemDef.newModelColor = new int [11];
			itemDef.editedModelColor[0] = 7860;
			itemDef.newModelColor[0] = 24006;
			itemDef.editedModelColor[1] = 7876;
			itemDef.newModelColor[1] = 24006;
			itemDef.editedModelColor[2] = 7892;
			itemDef.newModelColor[2] = 24006;
			itemDef.editedModelColor[3] = 7884;
			itemDef.newModelColor[3] = 24006;
			itemDef.editedModelColor[4] = 7868;
			itemDef.newModelColor[4] = 24006;
			itemDef.editedModelColor[5] = 7864;
			itemDef.newModelColor[5] = 24006;
			itemDef.editedModelColor[6] = 7880;
			itemDef.newModelColor[6] = 24006;
			itemDef.editedModelColor[7] = 7848;
			itemDef.newModelColor[7] = 24006;
			itemDef.editedModelColor[8] = 7888;
			itemDef.newModelColor[8] = 24006;
			itemDef.editedModelColor[9] = 7872;
			itemDef.newModelColor[9] = 24006;
			itemDef.editedModelColor[10] = 7856;
			itemDef.newModelColor[10] = 24006;
			itemDef.modelZoom = 2256;
			itemDef.rotationX = 456;
			itemDef.rotationY = 513;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffset1 = 0;
			itemDef.maleEquip1 = 51795;
			itemDef.femaleEquip1 = 51795;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			break;
		case 14007:
			itemDef.name = "Staff of light";
			itemDef.modelID = 51845;
			itemDef.editedModelColor = new int [11];
			itemDef.newModelColor = new int [11];
			itemDef.editedModelColor[0] = 7860;
			itemDef.newModelColor[0] = 14285;
			itemDef.editedModelColor[1] = 7876;
			itemDef.newModelColor[1] = 14285;
			itemDef.editedModelColor[2] = 7892;
			itemDef.newModelColor[2] = 14285;
			itemDef.editedModelColor[3] = 7884;
			itemDef.newModelColor[3] = 14285;
			itemDef.editedModelColor[4] = 7868;
			itemDef.newModelColor[4] = 14285;
			itemDef.editedModelColor[5] = 7864;
			itemDef.newModelColor[5] = 14285;
			itemDef.editedModelColor[6] = 7880;
			itemDef.newModelColor[6] = 14285;
			itemDef.editedModelColor[7] = 7848;
			itemDef.newModelColor[7] = 14285;
			itemDef.editedModelColor[8] = 7888;
			itemDef.newModelColor[8] = 14285;
			itemDef.editedModelColor[9] = 7872;
			itemDef.newModelColor[9] = 14285;
			itemDef.editedModelColor[10] = 7856;
			itemDef.newModelColor[10] = 14285;
			itemDef.modelZoom = 2256;
			itemDef.rotationX = 456;
			itemDef.rotationY = 513;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffset1 = 0;
			itemDef.maleEquip1 = 51795;
			itemDef.femaleEquip1 = 51795;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			break;
		case 14003:
			itemDef.name = "Robin hood hat";
			itemDef.modelID = 3021;
			itemDef.editedModelColor = new int [3];
			itemDef.newModelColor = new int [3];
			itemDef.editedModelColor[0] = 15009;
			itemDef.newModelColor[0] = 30847;
			itemDef.editedModelColor[1] = 17294;
			itemDef.newModelColor[1] = 32895;
			itemDef.editedModelColor[2] = 15252;
			itemDef.newModelColor[2] = 30847;
			itemDef.modelZoom = 650;
			itemDef.rotationY = 2044;
			itemDef.rotationX = 256;
			itemDef.modelOffset1 = -3;
			itemDef.modelOffsetY = -5;
			itemDef.maleEquip1 = 3378;
			itemDef.femaleEquip1 = 3382;
			itemDef.maleDialogue = 3378;
			itemDef.femaleDialogue = 3382;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			break;

		case 14001:
			itemDef.name = "Robin hood hat";
			itemDef.modelID = 3021;
			itemDef.editedModelColor = new int [3];
			itemDef.newModelColor = new int [3];
			itemDef.editedModelColor[0] = 15009;
			itemDef.newModelColor[0] = 10015;
			itemDef.editedModelColor[1] = 17294;
			itemDef.newModelColor[1] = 7730;
			itemDef.editedModelColor[2] = 15252;
			itemDef.newModelColor[2] = 7973;
			itemDef.modelZoom = 650;
			itemDef.rotationY = 2044;
			itemDef.rotationX = 256;
			itemDef.modelOffset1 = -3;
			itemDef.modelOffsetY = -5;
			itemDef.maleEquip1 = 3378;
			itemDef.femaleEquip1 = 3382;
			itemDef.maleDialogue = 3378;
			itemDef.femaleDialogue = 3382;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			break;

		case 14002:
			itemDef.name = "Robin hood hat";
			itemDef.modelID = 3021;
			itemDef.editedModelColor = new int [3];
			itemDef.newModelColor = new int [3];
			itemDef.editedModelColor[0] = 15009;
			itemDef.newModelColor[0] = 35489;
			itemDef.editedModelColor[1] = 17294;
			itemDef.newModelColor[1] = 37774;
			itemDef.editedModelColor[2] = 15252;
			itemDef.newModelColor[2] = 35732;
			itemDef.modelZoom = 650;
			itemDef.rotationY = 2044;
			itemDef.rotationX = 256;
			itemDef.modelOffset1 = -3;
			itemDef.modelOffsetY = -5;
			itemDef.maleEquip1 = 3378;
			itemDef.femaleEquip1 = 3382;
			itemDef.maleDialogue = 3378;
			itemDef.femaleDialogue = 3382;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			break;

		case 14000:
			itemDef.name = "Robin hood hat";
			itemDef.modelID = 3021;
			itemDef.editedModelColor = new int [3];
			itemDef.newModelColor = new int [3];
			itemDef.editedModelColor[0] = 15009;
			itemDef.newModelColor[0] = 3745;
			itemDef.editedModelColor[1] = 17294;
			itemDef.newModelColor[1] = 3982;
			itemDef.editedModelColor[2] = 15252;
			itemDef.newModelColor[2] = 3988;
			itemDef.modelZoom = 650;
			itemDef.rotationY = 2044;
			itemDef.rotationX = 256;
			itemDef.modelOffsetX = 1;
			itemDef.modelOffsetY = -5;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			itemDef.maleEquip1 = 3378;
			itemDef.femaleEquip1 = 3382;
			itemDef.maleDialogue = 3378;
			itemDef.femaleDialogue = 3382;
			break;

			/*case 19111:
			itemDef.name = "TokHaar-Kal";
			// itemDef.femaleOffset = 0;
			itemDef.value = 60000;
			itemDef.maleEquip1 = 62575;
			itemDef.femaleEquip1 = 62582;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.modelOffset1 = -4;
			itemDef.modelID = 62592;
			itemDef.stackable = false;
			itemDef.description = "A cape made of ancient, enchanted obsidian.";
			itemDef.modelZoom = 2086;
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			itemDef.modelOffsetY = 0;
			itemDef.rotationY = 533;
			itemDef.rotationX = 333;
			break;
			 */
		case 20000:
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			itemDef.modelID = 53835;
			itemDef.name = "Steadfast boots";
			itemDef.modelZoom = 900;
			itemDef.rotationY = 165;
			itemDef.rotationX = 99;
			itemDef.modelOffset1 = 3;
			itemDef.modelOffsetY = -7;
			itemDef.maleEquip1 = 53327;
			itemDef.femaleEquip1 = 53643;
			itemDef.description = "A pair of Steadfast boots.";
			break;

		case 20001:
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			itemDef.modelID = 53828;
			itemDef.name = "Glaiven boots";
			itemDef.modelZoom = 900;
			itemDef.rotationY = 165;
			itemDef.rotationX = 99;
			itemDef.modelOffset1 = 3;
			itemDef.modelOffsetY = -7;
			itemDef.femaleEquip1 = 53309;
			itemDef.maleEquip1 = 53309;
			itemDef.description = "A pair of Glaiven boots.";
			break;

		case 20002:
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[4] = "Drop";
			itemDef.description = "A pair of Ragefire boots.";
			itemDef.modelID = 53897;
			itemDef.name = "Ragefire boots";
			itemDef.modelZoom = 900;
			itemDef.rotationY = 165;
			itemDef.rotationX = 99;
			itemDef.modelOffset1 = 3;
			itemDef.modelOffsetY = -7;
			itemDef.maleEquip1 = 53330;
			itemDef.femaleEquip1 = 53651;
			break;

		case 14018:
			itemDef.modelID = 5324;
			itemDef.name = "Ornate katana";
			itemDef.modelZoom = 2025;
			itemDef.rotationX = 593;
			itemDef.rotationY = 2040;
			itemDef.modelOffset1 = 5;
			itemDef.modelOffsetY = 1;
			itemDef.value = 50000;
			itemDef.membersObject = true;
			itemDef.maleEquip1 = 5324;
			itemDef.femaleEquip1 = 5324;
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wield";
			itemDef.actions[4] = "Destroy";
			break;
		case 14008:
			itemDef.modelID = 62714;
			itemDef.name = "Torva full helm";
			itemDef.description = "Torva full helm";
			itemDef.modelZoom = 672;
			itemDef.rotationY = 85;
			itemDef.rotationX = 1867;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffsetY = -3;
			itemDef.maleEquip1 = 62738;
			itemDef.femaleEquip1 = 62754;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[2] = "Check-charges";
			itemDef.actions[4] = "Drop";
			itemDef.maleDialogue = 62729;
			itemDef.femaleDialogue = 62729;
			break;
		case 14009:
			itemDef.modelID = 62699;
			itemDef.name = "Torva platebody";
			itemDef.description = "Torva platebody";
			itemDef.modelZoom = 1506;
			itemDef.rotationY = 473;
			itemDef.rotationX = 2042;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffsetY = 0;
			itemDef.maleEquip1 = 62746;
			itemDef.femaleEquip1 = 62762;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[2] = "Check-charges";
			itemDef.actions[4] = "Drop";
			break;

		case 14010:
			itemDef.modelID = 62701;
			itemDef.name = "Torva platelegs";
			itemDef.description = "Torva platelegs";
			itemDef.modelZoom = 1740;
			itemDef.rotationY = 474;
			itemDef.rotationX = 2045;
			itemDef.modelOffset1 = 0;
			itemDef.modelOffsetY = -5;
			itemDef.maleEquip1 = 62743;
			itemDef.femaleEquip1 = 62760;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[2] = "Check-charges";
			itemDef.actions[4] = "Drop";
			break;

		case 14011:
			itemDef.modelID = 62693;
			itemDef.name = "Pernix cowl";
			itemDef.description = "Pernix cowl";
			itemDef.modelZoom = 800;
			itemDef.rotationY = 532;
			itemDef.rotationX = 14;
			itemDef.modelOffset1 = -1;
			itemDef.modelOffsetY = 1;
			itemDef.maleEquip1 = 62739;
			itemDef.femaleEquip1 = 62756;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[2] = "Check-charges";
			itemDef.actions[4] = "Drop";
			itemDef.maleDialogue = 62731;
			itemDef.femaleDialogue = 62727;
			itemDef.editedModelColor = new int[2];
			itemDef.newModelColor = new int[2];
			itemDef.editedModelColor[0] = 4550;
			itemDef.newModelColor[0] = 0;
			itemDef.editedModelColor[1] = 4540;
			itemDef.newModelColor[1] = 0;
			break;

		case 14012:
			itemDef.modelID = 62709;
			itemDef.name = "Pernix body";
			itemDef.description = "Pernix body";
			itemDef.modelZoom = 1378;
			itemDef.rotationY = 485;
			itemDef.rotationX = 2042;
			itemDef.modelOffset1 = -1;
			itemDef.modelOffsetY = 7;
			itemDef.maleEquip1 = 62744;
			itemDef.femaleEquip1 = 62765;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[2] = "Check-charges";
			itemDef.actions[4] = "Drop";
			break;

		case 14013:
			itemDef.modelID = 62695;
			itemDef.name = "Pernix chaps";
			itemDef.description = "Pernix chaps";
			itemDef.modelZoom = 1740;
			itemDef.rotationY = 504;
			itemDef.rotationX = 0;
			itemDef.modelOffset1 = 4;
			itemDef.modelOffsetY = 3;
			itemDef.maleEquip1 = 62741;
			itemDef.femaleEquip1 = 62757;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[2] = "Check-charges";
			itemDef.actions[4] = "Drop";
			break;
		case 14014:
			itemDef.modelID = 62710;
			itemDef.name = "Virtus mask";
			itemDef.description = "Virtus mask";
			itemDef.modelZoom = 928;
			itemDef.rotationY = 406;
			itemDef.rotationX = 2041;
			itemDef.modelOffset1 = 1;
			itemDef.modelOffsetY = -5;
			itemDef.maleEquip1 = 62736;
			itemDef.femaleEquip1 = 62755;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[2] = "Check-charges";
			itemDef.actions[4] = "Drop";
			itemDef.maleDialogue = 62728;
			itemDef.femaleDialogue = 62728;
			break;

		case 14015:
			itemDef.modelID = 62704;
			itemDef.name = "Virtus robe top";
			itemDef.description = "Virtus robe top";
			itemDef.modelZoom = 1122;
			itemDef.rotationY = 488;
			itemDef.rotationX = 3;
			itemDef.modelOffset1 = 1;
			itemDef.modelOffsetY = 0;
			itemDef.maleEquip1 = 62748;
			itemDef.femaleEquip1 = 62764;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[2] = "Check-charges";
			itemDef.actions[4] = "Drop";
			break;

		case 14016:
			itemDef.modelID = 62700;
			itemDef.name = "Virtus robe legs";
			itemDef.description = "Virtus robe legs";
			itemDef.modelZoom = 1740;
			itemDef.rotationY = 498;
			itemDef.rotationX = 2045;
			itemDef.modelOffset1 = -1;
			itemDef.modelOffsetY = 4;
			itemDef.maleEquip1 = 62742;
			itemDef.femaleEquip1 = 62758;
			itemDef.groundActions = new String[5];
			itemDef.groundActions[2] = "Take";
			itemDef.actions = new String[5];
			itemDef.actions[1] = "Wear";
			itemDef.actions[2] = "Check-charges";
			itemDef.actions[4] = "Drop";
			break;
		}
		return itemDef;
	}

	private void readValues(Stream stream) {
		do {
			int i = stream.readUnsignedByte();
			if (i == 0)
				return;
			if (i == 1) {
				modelID = stream.readUnsignedWord();
			} else if (i == 2)
				name = stream.readString();
			else if (i == 3)
				description = stream.readString();
			else if (i == 4)
				modelZoom = stream.readUnsignedWord();
			else if (i == 5)
				rotationY = stream.readUnsignedWord();
			else if (i == 6)
				rotationX = stream.readUnsignedWord();
			else if (i == 7) {
				modelOffset1 = stream.readUnsignedWord();
				if (modelOffset1 > 32767)
					modelOffset1 -= 0x10000;
			} else if (i == 8) {
				modelOffsetY = stream.readUnsignedWord();
				if (modelOffsetY > 32767)
					modelOffsetY -= 0x10000;
			} else if (i == 10)
				stream.readUnsignedWord();
			else if (i == 11)
				stackable = true;
			else if (i == 12)
				value = stream.readUnsignedWord();
			else if (i == 16)
				membersObject = true;
			else if (i == 23) {
				maleEquip1 = stream.readUnsignedWord();
				maleYOffset = stream.readSignedByte();
			} else if (i == 24)
				maleEquip2 = stream.readUnsignedWord();
			else if (i == 25) {
				femaleEquip1 = stream.readUnsignedWord();
				femaleYOffset = stream.readSignedByte();
			} else if (i == 26)
				femaleEquip2 = stream.readUnsignedWord();
			else if (i >= 30 && i < 35) {
				if (groundActions == null)
					groundActions = new String[5];
				groundActions[i - 30] = stream.readString();
				if (groundActions[i - 30].equalsIgnoreCase("hidden"))
					groundActions[i - 30] = null;
			} else if (i >= 35 && i < 40) {
				if (actions == null)
					actions = new String[5];
				actions[i - 35] = stream.readString();
				if (actions[i - 35].equalsIgnoreCase("null"))
					actions[i - 35] = null;
			} else if (i == 40) {
				int j = stream.readUnsignedByte();
				editedModelColor = new int[j];
				newModelColor = new int[j];
				for (int k = 0; k < j; k++) {
					editedModelColor[k] = stream.readUnsignedWord();
					newModelColor[k] = stream.readUnsignedWord();
				}
			} else if (i == 78)
				maleEquip3 = stream.readUnsignedWord();
			else if (i == 79)
				femaleEquip3 = stream.readUnsignedWord();
			else if (i == 90)
				maleDialogue = stream.readUnsignedWord();
			else if (i == 91)
				femaleDialogue = stream.readUnsignedWord();
			else if (i == 92)
				maleDialogueModel = stream.readUnsignedWord();
			else if (i == 93)
				femaleDialogueModel = stream.readUnsignedWord();
			else if (i == 95)
				modelOffsetX = stream.readUnsignedWord();
			else if (i == 97)
				certID = stream.readUnsignedWord();
			else if (i == 98)
				certTemplateID = stream.readUnsignedWord();
			else if (i >= 100 && i < 110) {
				if (stackIDs == null) {
					stackIDs = new int[10];
					stackAmounts = new int[10];
				}
				stackIDs[i - 100] = stream.readUnsignedWord();
				stackAmounts[i - 100] = stream.readUnsignedWord();
			} else if (i == 110)
				sizeX = stream.readUnsignedWord();
			else if (i == 111)
				sizeY = stream.readUnsignedWord();
			else if (i == 112)
				sizeZ = stream.readUnsignedWord();
			else if (i == 113)
				shadow = stream.readSignedByte();
			else if (i == 114)
				lightness = stream.readSignedByte() * 5;
			else if (i == 115)
				team = stream.readUnsignedByte();
			else if (i == 116)
				lendID = stream.readUnsignedWord();
			else if (i == 117)
				lentItemID = stream.readUnsignedWord();
		} while (true);
	}

	public static void setSettings() {
		try {
			prices = new int[22694];
			int index = 0;
			for (String line : Files.readAllLines(Paths.get(signlink.findcachedir() + "data/data.txt"))) {
				prices[index] = Integer.parseInt(line);
				index++;
			}
			for(int i : UNTRADEABLE_ITEMS) {
				untradeableItems.add(i);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void toNote() {
		ItemDef itemDef = forID(certTemplateID);
		modelID = itemDef.modelID;
		modelZoom = itemDef.modelZoom;
		rotationY = itemDef.rotationY;
		rotationX = itemDef.rotationX;
		modelOffsetX = itemDef.modelOffsetX;
		modelOffset1 = itemDef.modelOffset1;
		modelOffsetY = itemDef.modelOffsetY;
		editedModelColor = itemDef.editedModelColor;
		newModelColor = itemDef.newModelColor;
		ItemDef itemDef_1 = forID(certID);
		name = itemDef_1.name;
		membersObject = itemDef_1.membersObject;
		value = itemDef_1.value;
		String s = "a";
		char c = itemDef_1.name.charAt(0);
		if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U')
			s = "an";
		description = ("Swap this note at any bank for " + s + " " + itemDef_1.name + ".");
		stackable = true;
	}

	private void toLend() {
		ItemDef itemDef = forID(lentItemID);
		actions = new String[5];
		modelID = itemDef.modelID;
		modelOffset1 = itemDef.modelOffset1;
		rotationX = itemDef.rotationX;
		modelOffsetY = itemDef.modelOffsetY;
		modelZoom = itemDef.modelZoom;
		rotationY = itemDef.rotationY;
		modelOffsetX = itemDef.modelOffsetX;
		value = 0;
		ItemDef itemDef_1 = forID(lendID);
		maleDialogueModel = itemDef_1.maleDialogueModel;
		editedModelColor = itemDef_1.editedModelColor;
		maleEquip3 = itemDef_1.maleEquip3;
		maleEquip2 = itemDef_1.maleEquip2;
		femaleDialogueModel = itemDef_1.femaleDialogueModel;
		maleDialogue = itemDef_1.maleDialogue;
		groundActions = itemDef_1.groundActions;
		maleEquip1 = itemDef_1.maleEquip1;
		name = itemDef_1.name;
		femaleEquip1 = itemDef_1.femaleEquip1;
		membersObject = itemDef_1.membersObject;
		femaleDialogue = itemDef_1.femaleDialogue;
		femaleEquip2 = itemDef_1.femaleEquip2;
		femaleEquip3 = itemDef_1.femaleEquip3;
		newModelColor = itemDef_1.newModelColor;
		team = itemDef_1.team;
		if (itemDef_1.actions != null) {
			for (int i_33_ = 0; i_33_ < 4; i_33_++)
				actions[i_33_] = itemDef_1.actions[i_33_];
		}
		actions[4] = "Discard";
	}

	public static Sprite getSprite(int i, int j, int k, int zoom) {
		if (k == 0 && zoom != -1) {
			Sprite sprite = (Sprite) spriteCache.get(i);
			if (sprite != null && sprite.maxHeight != j && sprite.maxHeight != -1) {
				sprite.unlink();
				sprite = null;
			}
			if (sprite != null)
				return sprite;
		}
		ItemDef itemDef = forID(i);
		if (itemDef.stackIDs == null)
			j = -1;
		if (j > 1) {
			int i1 = -1;
			for (int j1 = 0; j1 < 10; j1++)
				if (j >= itemDef.stackAmounts[j1] && itemDef.stackAmounts[j1] != 0)
					i1 = itemDef.stackIDs[j1];

			if (i1 != -1)
				itemDef = forID(i1);
		}
		Model model = itemDef.getItemModelFinalised(1);
		if (model == null)
			return null;
		Sprite sprite = null;
		if (itemDef.certTemplateID != -1) {
			sprite = getSprite(itemDef.certID, 10, -1);
			if (sprite == null)
				return null;
		}
		if (itemDef.lendID != -1) {
			sprite = getSprite(itemDef.lendID, 50, 0);
			if (sprite == null)
				return null;
		}
		Sprite sprite2 = new Sprite(32, 32);
		int k1 = Rasterizer.center_x;
		int l1 = Rasterizer.center_y;
		int ai[] = Rasterizer.lineOffsets;
		int ai1[] = DrawingArea.pixels;
		int i2 = DrawingArea.width;
		int j2 = DrawingArea.height;
		int k2 = DrawingArea.topX;
		int l2 = DrawingArea.bottomX;
		int i3 = DrawingArea.topY;
		int j3 = DrawingArea.bottomY;
		Rasterizer.notTextured = false;
		DrawingArea.initDrawingArea(32, 32, sprite2.myPixels);
		DrawingArea.drawPixels(32, 0, 0, 0, 32);
		Rasterizer.setDefaultBounds();
		int k3 = itemDef.modelZoom;
		if (zoom != -1 && zoom != 0)
			k3 = (itemDef.modelZoom * 100) / zoom;
		if (k == -1)
			k3 = (int) ((double) k3 * 1.5D);
		if (k > 0)
			k3 = (int) ((double) k3 * 1.04D);
		int l3 = Rasterizer.SINE[itemDef.rotationY] * k3 >> 16;
			int i4 = Rasterizer.COSINE[itemDef.rotationY] * k3 >> 16;
			model.renderSingle(itemDef.rotationX, itemDef.modelOffsetX, itemDef.rotationY, itemDef.modelOffset1, l3 + model.modelHeight / 2 + itemDef.modelOffsetY, i4 + itemDef.modelOffsetY);
			for (int i5 = 31; i5 >= 0; i5--) {
				for (int j4 = 31; j4 >= 0; j4--) {
					if (sprite2.myPixels[i5 + j4 * 32] != 0)
						continue;
					if (i5 > 0 && sprite2.myPixels[(i5 - 1) + j4 * 32] > 1) {
						sprite2.myPixels[i5 + j4 * 32] = 1;
						continue;
					}
					if (j4 > 0 && sprite2.myPixels[i5 + (j4 - 1) * 32] > 1) {
						sprite2.myPixels[i5 + j4 * 32] = 1;
						continue;
					}
					if (i5 < 31 && sprite2.myPixels[i5 + 1 + j4 * 32] > 1) {
						sprite2.myPixels[i5 + j4 * 32] = 1;
						continue;
					}
					if (j4 < 31 && sprite2.myPixels[i5 + (j4 + 1) * 32] > 1)
						sprite2.myPixels[i5 + j4 * 32] = 1;
				}

			}

			if (k > 0) {
				for (int j5 = 31; j5 >= 0; j5--) {
					for (int k4 = 31; k4 >= 0; k4--) {
						if (sprite2.myPixels[j5 + k4 * 32] != 0)
							continue;
						if (j5 > 0 && sprite2.myPixels[(j5 - 1) + k4 * 32] == 1) {
							sprite2.myPixels[j5 + k4 * 32] = k;
							continue;
						}
						if (k4 > 0 && sprite2.myPixels[j5 + (k4 - 1) * 32] == 1) {
							sprite2.myPixels[j5 + k4 * 32] = k;
							continue;
						}
						if (j5 < 31 && sprite2.myPixels[j5 + 1 + k4 * 32] == 1) {
							sprite2.myPixels[j5 + k4 * 32] = k;
							continue;
						}
						if (k4 < 31 && sprite2.myPixels[j5 + (k4 + 1) * 32] == 1)
							sprite2.myPixels[j5 + k4 * 32] = k;
					}

				}

			} else if (k == 0) {
				for (int k5 = 31; k5 >= 0; k5--) {
					for (int l4 = 31; l4 >= 0; l4--)
						if (sprite2.myPixels[k5 + l4 * 32] == 0 && k5 > 0 && l4 > 0 && sprite2.myPixels[(k5 - 1) + (l4 - 1) * 32] > 0)
							sprite2.myPixels[k5 + l4 * 32] = 0x302020;

				}

			}
			if (itemDef.certTemplateID != -1) {
				int l5 = sprite.maxWidth;
				int j6 = sprite.maxHeight;
				sprite.maxWidth = 32;
				sprite.maxHeight = 32;
				sprite.drawSprite(0, 0);
				sprite.maxWidth = l5;
				sprite.maxHeight = j6;
			}
			if (itemDef.lendID != -1) {
				int l5 = sprite.maxWidth;
				int j6 = sprite.maxHeight;
				sprite.maxWidth = 32;
				sprite.maxHeight = 32;
				sprite.drawSprite(0, 0);
				sprite.maxWidth = l5;
				sprite.maxHeight = j6;
			}
			if (k == 0)
				spriteCache.put(sprite2, i);
			DrawingArea.initDrawingArea(j2, i2, ai1);
			DrawingArea.setDrawingArea(j3, k2, l2, i3);
			Rasterizer.center_x = k1;
			Rasterizer.center_y = l1;
			Rasterizer.lineOffsets = ai;
			Rasterizer.notTextured = true;
			sprite2.maxWidth = itemDef.stackable ? 33 : 32;
			sprite2.maxHeight = j;
			return sprite2;
	}

	public static Sprite getSprite(int i, int j, int k) {
		if (k == 0) {
			Sprite sprite = (Sprite) spriteCache.get(i);
			if (sprite != null && sprite.maxHeight != j && sprite.maxHeight != -1) {
				sprite.unlink();
				sprite = null;
			}
			if (sprite != null)
				return sprite;
		}
		ItemDef itemDef = forID(i);
		if (itemDef.stackIDs == null)
			j = -1;
		if (j > 1) {
			int i1 = -1;
			for (int j1 = 0; j1 < 10; j1++)
				if (j >= itemDef.stackAmounts[j1] && itemDef.stackAmounts[j1] != 0)
					i1 = itemDef.stackIDs[j1];
			if (i1 != -1)
				itemDef = forID(i1);
		}
		Model model = itemDef.getItemModelFinalised(1);
		if (model == null)
			return null;
		Sprite sprite = null;
		if (itemDef.certTemplateID != -1) {
			sprite = getSprite(itemDef.certID, 10, -1);
			if (sprite == null)
				return null;
		}
		if (itemDef.lentItemID != -1) {
			sprite = getSprite(itemDef.lendID, 50, 0);
			if (sprite == null)
				return null;
		}
		Sprite sprite2 = new Sprite(32, 32);
		int k1 = Rasterizer.center_x;
		int l1 = Rasterizer.center_y;
		int ai[] = Rasterizer.lineOffsets;
		int ai1[] = DrawingArea.pixels;
		int i2 = DrawingArea.width;
		int j2 = DrawingArea.height;
		int k2 = DrawingArea.topX;
		int l2 = DrawingArea.bottomX;
		int i3 = DrawingArea.topY;
		int j3 = DrawingArea.bottomY;
		Rasterizer.notTextured = false;
		DrawingArea.initDrawingArea(32, 32, sprite2.myPixels);
		DrawingArea.drawPixels(32, 0, 0, 0, 32);
		Rasterizer.setDefaultBounds();
		int k3 = itemDef.modelZoom;
		if (k == -1)
			k3 = (int) ((double) k3 * 1.5D);
		if (k > 0)
			k3 = (int) ((double) k3 * 1.04D);
		int l3 = Rasterizer.SINE[itemDef.rotationY] * k3 >> 16;
		int i4 = Rasterizer.COSINE[itemDef.rotationY] * k3 >> 16;
		model.renderSingle(itemDef.rotationX, itemDef.modelOffsetX, itemDef.rotationY, itemDef.modelOffset1, l3 + model.modelHeight / 2 + itemDef.modelOffsetY, i4 + itemDef.modelOffsetY);
		for (int i5 = 31; i5 >= 0; i5--) {
			for (int j4 = 31; j4 >= 0; j4--)
				if (sprite2.myPixels[i5 + j4 * 32] == 0)
					if (i5 > 0 && sprite2.myPixels[(i5 - 1) + j4 * 32] > 1)
						sprite2.myPixels[i5 + j4 * 32] = 1;
					else if (j4 > 0 && sprite2.myPixels[i5 + (j4 - 1) * 32] > 1)
						sprite2.myPixels[i5 + j4 * 32] = 1;
					else if (i5 < 31 && sprite2.myPixels[i5 + 1 + j4 * 32] > 1)
						sprite2.myPixels[i5 + j4 * 32] = 1;
					else if (j4 < 31 && sprite2.myPixels[i5 + (j4 + 1) * 32] > 1)
						sprite2.myPixels[i5 + j4 * 32] = 1;
		}
		if (k > 0) {
			for (int j5 = 31; j5 >= 0; j5--) {
				for (int k4 = 31; k4 >= 0; k4--)
					if (sprite2.myPixels[j5 + k4 * 32] == 0)
						if (j5 > 0 && sprite2.myPixels[(j5 - 1) + k4 * 32] == 1)
							sprite2.myPixels[j5 + k4 * 32] = k;
						else if (k4 > 0 && sprite2.myPixels[j5 + (k4 - 1) * 32] == 1)
							sprite2.myPixels[j5 + k4 * 32] = k;
						else if (j5 < 31 && sprite2.myPixels[j5 + 1 + k4 * 32] == 1)
							sprite2.myPixels[j5 + k4 * 32] = k;
						else if (k4 < 31 && sprite2.myPixels[j5 + (k4 + 1) * 32] == 1)
							sprite2.myPixels[j5 + k4 * 32] = k;
			}
		} else if (k == 0) {
			for (int k5 = 31; k5 >= 0; k5--) {
				for (int l4 = 31; l4 >= 0; l4--)
					if (sprite2.myPixels[k5 + l4 * 32] == 0 && k5 > 0 && l4 > 0 && sprite2.myPixels[(k5 - 1) + (l4 - 1) * 32] > 0)
						sprite2.myPixels[k5 + l4 * 32] = 0x302020;
			}
		}
		if (itemDef.certTemplateID != -1) {
			int l5 = sprite.maxWidth;
			int j6 = sprite.maxHeight;
			sprite.maxWidth = 32;
			sprite.maxHeight = 32;
			sprite.drawSprite(0, 0);
			sprite.maxWidth = l5;
			sprite.maxHeight = j6;
		}
		if (itemDef.lentItemID != -1) {
			int l5 = sprite.maxWidth;
			int j6 = sprite.maxHeight;
			sprite.maxWidth = 32;
			sprite.maxHeight = 32;
			sprite.drawSprite(0, 0);
			sprite.maxWidth = l5;
			sprite.maxHeight = j6;
		}
		if (k == 0)
			spriteCache.put(sprite2, i);
		DrawingArea.initDrawingArea(j2, i2, ai1);
		DrawingArea.setDrawingArea(j3, k2, l2, i3);
		Rasterizer.center_x = k1;
		Rasterizer.center_y = l1;
		Rasterizer.lineOffsets = ai;
		Rasterizer.notTextured = true;
		if (itemDef.stackable)
			sprite2.maxWidth = 33;
		else
			sprite2.maxWidth = 32;
		sprite2.maxHeight = j;
		return sprite2;
	}

	public Model getItemModelFinalised(int amount) {
		if (stackIDs != null && amount > 1) {
			int stackId = -1;
			for (int k = 0; k < 10; k++)
				if (amount >= stackAmounts[k] && stackAmounts[k] != 0)
					stackId = stackIDs[k];
			if (stackId != -1)
				return forID(stackId).getItemModelFinalised(1);
		}
		Model model = (Model) modelCache.get(id);
		if (model != null)
			return model;
		model = Model.fetchModel(modelID);
		if (model == null)
			return null;
		if (sizeX != 128 || sizeY != 128 || sizeZ != 128)
			model.scaleT(sizeX, sizeZ, sizeY);
		if (editedModelColor != null) {
			for (int l = 0; l < editedModelColor.length; l++)
				model.recolour(editedModelColor[l], newModelColor[l]);
		}
		model.light(64 + shadow, 768 + lightness, -50, -10, -50, true);
		model.rendersWithinOneTile = true;
		modelCache.put(model, id);
		return model;
	}

	public Model getItemModel(int i) {
		if (stackIDs != null && i > 1) {
			int j = -1;
			for (int k = 0; k < 10; k++)
				if (i >= stackAmounts[k] && stackAmounts[k] != 0)
					j = stackIDs[k];
			if (j != -1)
				return forID(j).getItemModel(1);
		}
		Model model = Model.fetchModel(modelID);
		if (model == null)
			return null;
		if (editedModelColor != null) {
			for (int l = 0; l < editedModelColor.length; l++)
				model.recolour(editedModelColor[l], newModelColor[l]);
		}
		return model;
	}

	public static final int[] UNTRADEABLE_ITEMS = 
		{13661, 13262,
		6529, 6950, 1464, 2996, 2677, 2678, 2679, 2680, 2682, 
		2683, 2684, 2685, 2686, 2687, 2688, 2689, 2690, 
		6570, 12158, 12159, 12160, 12163, 12161, 12162,
		19143, 19149, 19146, 19157, 19162, 19152, 4155,
		8850, 10551, 8839, 8840, 8842, 11663, 11664, 
		11665, 3842, 3844, 3840, 8844, 8845, 8846, 8847, 
		8848, 8849, 8850, 10551, 7462, 7461, 7460, 
		7459, 7458, 7457, 7456, 7455, 7454, 7453, 8839, 
		8840, 8842, 11663, 11664, 11665, 10499, 9748, 
		9754, 9751, 9769, 9757, 9760, 9763, 9802, 9808,
		9784, 9799, 9805, 9781, 9796, 9793, 9775, 9772,
		9778, 9787, 9811, 9766, 9749, 9755, 9752, 9770, 
		9758, 9761, 9764, 9803, 9809, 9785, 9800, 9806, 
		9782, 9797, 9794, 9776, 9773, 9779, 9788, 9812, 
		9767, 9747, 9753, 9750, 9768, 9756, 9759, 9762,
		9801, 9807, 9783, 9798, 9804, 9780, 9795, 9792, 
		9774, 9771, 9777, 9786, 9810, 9765, 9948, 9949,
		9950, 12169, 12170, 12171, 20671, 14641, 14642,
		6188, 10954, 10956, 10958,
		3057, 3058, 3059, 3060, 3061,
		7594, 7592, 7593, 7595, 7596,
		14076, 14077, 14081,
		10840, 10836, 6858, 6859, 10837, 10838, 10839,
		9925, 9924, 9923, 9922, 9921,
		4084, 4565, 20046, 20044, 20045,
		1050, 14595, 14603, 14602, 14605, 	11789,
		19708, 19706, 19707,
		4860, 4866, 4872, 4878, 4884, 4896, 4890, 4896, 4902,
		4932, 4938, 4944, 4950, 4908, 4914, 4920, 4926, 4956,
		4926, 4968, 4994, 4980, 4986, 4992, 4998,
		18778, 18779, 18780, 18781,
		13450, 13444, 13405, 15502, 
		10548, 10549, 10550, 10551, 10555, 10552, 10553, 2412, 2413, 2414,
		20747, 
		18365, 18373, 18371, 15246, 12964, 12971, 12978, 14017,
		757, 8851,
		13855, 13848, 13857, 13856, 13854, 13853, 13852, 13851, 13850, 5509, 13653, 14021, 14020, 19111, 14019, 14022,
		19785, 19786, 18782, 18351, 18349, 18353, 18357, 18355, 18359, 18335
		};

	public ItemDef() {
		id = -1;
	}

	public byte femaleYOffset;
	public int value;
	public int[] editedModelColor;
	public int id;
	public static MemCache spriteCache = new MemCache(100);
	public static MemCache modelCache = new MemCache(50);
	public int[] newModelColor;
	public boolean membersObject;
	public int femaleEquip3;
	public int certTemplateID;
	public int femaleEquip2;
	public int maleEquip1;
	public int maleDialogueModel;
	public int sizeX;
	public String groundActions[];
	public int modelOffset1;
	public String name;
	public static ItemDef[] cache;
	public int femaleDialogueModel;
	public int modelID;
	public int maleDialogue;
	public boolean stackable;
	public String description;
	public int certID;
	public static int cacheIndex;
	public int modelZoom;
	public static Stream stream;
	public int lightness;
	public int maleEquip3;
	public int maleEquip2;
	public String actions[];
	public int rotationY;
	public int sizeZ;
	public int sizeY;
	public int[] stackIDs;
	public int modelOffsetY;
	public static int[] streamIndices;
	public int shadow;
	public int femaleDialogue;
	public int rotationX;
	public int femaleEquip1;
	public int[] stackAmounts;
	public int team;
	public static int totalItems;
	public int modelOffsetX;
	public byte maleYOffset;
	public byte maleXOffset;
	public int lendID;
	public int lentItemID;
	public boolean untradeable;
}