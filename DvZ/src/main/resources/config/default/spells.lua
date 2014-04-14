require 'SpellLibrary'

function crystalPerPlayer(player, target, block, location)
    spell.ItemCrystalStorage(player, false)
    spell.ItemPotionEffect(player, "15:60:0")
    
    return true
end

function crystalGlobal(player, target, block, location)
    spell.ItemCrystalStorage(player, true)
    spell.ItemPotionEffect(player, "15:60:0")
    
    return true
end

function internDiamond(player, target, block, location)
    if(spell.ItemArmorCheck(target, false)) then
        spell.ItemArmorSet(target, "!minecraft:diamond_helmet 0 1", "!minecraft:diamond_chestplate 0 1", "!minecraft:diamond_leggings 0 1", "!minecraft:diamond_boots 0 1")
        
        return true
    end
    
    return false
end

function internConcrete(player, target, block, location)
    local success, pPos = spell.ItemVariableSet("playerPos", player)

    if(success) then
        spell.ItemReplace(pPos, 2, 4, 0, 98, 0)
        spell.ItemReplace(pPos, 2, 98, 2, 98, 0)
    
        return true
    end
    
    return false
end

function internDwarfBow(player, target, block, location)
    spell.ItemGetItem(player, "!minecraft:arrow 0 5", 1)
    spell.ItemSetDamage(player, 0)

    return true
end

function internDwarfPotion(player, target, block, location)
    spell.ItemDamage(player, -4)

    return true
end

--Transmute Books
function internBuilderBook(player, target, block, location)
    if(spell.ItemClassCheck(player, "dwarf", 0)) then
        spell.ItemTransmute(player, 0, 0, 0, "", false, "!minecraft:stonebrick 0 64 40", "!minecraft:stonebrick 1 64 40", "!minecraft:stonebrick 2 64 40", "!minecraft:cobblestone 0 64 40")
        
        dwarfGetExperience(player, 3)
        return true
    end
    
    return false
end

function internAlchemyBook(player, target, block, location)
    if(spell.ItemClassCheck(player, "dwarf", 1)) then
        if(spell.ItemTransmute(player, 373, 64, 3, "lang:string_default_alchemy_fail", false, "!minecraft:bone 0 9", "!minecraft:potion 8421 1 20", "!minecraft:potion 8421 1 20", "!minecraft:potion 8421 1 20", "!minecraft:potion 8421 1 20", "!minecraft:potion 11449 1 20", "373 16274 1 20", "!minecraft:potion 16310 1 20", "!minecraft:milk_bucket 0 5 20", "!minecraft:cake 0 5 20", "!minecraft:potion 8259 1 5")) then
            dwarfSpecialClassSuccess(player)
            return true
        end
    end
    
    return false
end

function internBlacksmithBook(player, target, block, location)
    if(spell.ItemClassCheck(player, "dwarf", 2)) then
        if(spell.ItemTransmute(player, 347, 0, 3, "lang:string_default_blacksmith_fail", false, "!minecraft:coal 0 10", "!minecraft:redstone_ore 0 8", "!minecraft:diamond_sword 0 1 45", "!minecraft:iron_sword 0 1 45", "!minecraft:diamond_axe 0 1 45", "!minecraft:string 0 3 45", "!minecraft:feather 0 32 50", "!minecraft:flint 0 32 50")) then
            dwarfSpecialClassSuccess(player)
            return true
        end
    end
    
    return false
end

function internTailorBook(player, target, block, location)
    if(spell.ItemClassCheck(player, "dwarf", 3)) then
        if(spell.ItemTransmute(player, 297, 0, 3, "lang:string_default_tailor_fail", false, "!minecraft:gold_ore 0 1", "!minecraft:diamond_helmet 0 1:2 40", "!minecraft:diamond_chestplate 0 1:2 40", "!minecraft:diamond_leggings 0 1:2 40", "!minecraft:diamond_boots 0 1:2 40")) then
            dwarfSpecialClassSuccess(player)
            return true
        end
    end
    
    return false
end

function dwarfSpecialClassSuccess(player)
    dwarfGetExperience(player, 6)
    spell.ItemRewardPoints(player, 32)
    spell.ItemDvZClassPoint(player, 1)
end

function dwarfGetExperience(player, ammount)
    spell.ItemExperience(player, ammount)
end

--Monsters
function internSuicidePill(player, target, block, location)
    spell.ItemKill(player)
    
    return true
end

function internCreeperExplode(player, target, block, location)
    local success, pPos = spell.ItemVariableSet("playerPos", player)

    if(success) then
        spell.ItemExplode(6, pPos)
        spell.ItemKill(player)
        
        return true
    end
    
    return false
end

function internSpiderConfuse(player, target, block, location)
    spell.ItemConfuse(target, 300, 0)
    
    return true
end

function internSpiderPosion(player, target, block, location)
    spell.ItemPotionEffect(target, "19:60:4")
    
    return true
end

function internIronGolemSmash(player, target, block, location)
    local success, bPos = spell.ItemVariableSet("blockPos", block)

    if(success) then
        spell.ItemSmash(bPos, true)

        return true
    end
    
    return false
end

function internIronGolemLeap(player, target, block, location)
    spell.ItemLeap(player, 4, 1.5, 1, true)
    
    return true
end

function internSnowGolemGet(player, target, block, location)
    spell.ItemGetItem(player, "!spellitems:internSnowGolemBall 0 8:16", 20)
    
    return true
end

function internSnowGolemBall(player, target, block, location)
    if(spell.ItemSnowballs(player, 96, "You need 96 Snowballs!")) then
        return true
    end
    
    return false
end

function internBroodmotherRoar(player, target, block, location)
    spell.ItemRoar(player, 15, "A Broodmother roars!")
    
    return true
end

function internBroodmotherLay(player, target, block, location)
    spell.ItemLay(player, 1, "A Broodmother is laying her eggs!")
    
    return true
end

function internEndermanBlink(player, target, block, location)
    if(spell.ItemBlink(player, 75)) then
        return true
    end
    
    return false
end

function internEndermanPortal(player, target, block, location)
    local success, pPos = spell.ItemVariableSet("playerPos", player)

    if(success) then
        if(spell.ItemPortal(player, pPos)) then
            spell.ItemGetItem(player, "!spellitems:internEndermanReinforcePortal", 1)
            return true
        end
    end
    
    return false
end

function internEndermanReinforcePortal(player, target, block, location)
    local success, pPos = spell.ItemVariableSet("playerPos", player)

    if(success) then
        spell.ItemReinforcePortal(player, pPos)
        
        return true
    end
    
    return false
end

function internCatHunger(player, target, block, location)
    if(spell.ItemHunger(target, 2)) then
        return true
    end
    
    return false
end

function internCatSteal(player, target, block, location)
    if(spell.ItemDrop(target)) then
        return true
    end
    
    return false
end

function internCreeperExplodeNew(player, target, block, location)
    local success, pPos = spell.ItemVariableSet("playerPos", player)

    if(success) then
        spell.ItemExplode(6, pPos)
        spell.ItemKill(player)
        
        return true
    end
    
    return false
end

--Reward Items
function rewardPoint(player, target, block, location)
    spell.ItemGetItem(player, "!minecraft:bow 0 1 10 poisonous:0,-10:0 Bow of ...", 1)
    spell.ItemGetItem(player, "!minecraft:snowball 0 32 10 freezing:0,-10:0 Freezing Snowballs", 1)
    spell.ItemGetItem(player, "!spellitems:lightningsword 0 1 10 -10:0", 1)
    spell.ItemGetItem(player, "!spellitems:potionpie 0 4 10", 1)
    spell.ItemGetItem(player, "!minecraft:blaze_rod 0 1 10 20:1 Fire Rod", 1)
    
    return true
end

function lightningsword(player, target, block, location)
    item.ItemDamage(target, 60)
end

function potionpie(player, target, block, location)
    if(math.random(0, 1)==0) then
        spell.ItemPotionEffect(player, "5:600:0")
    end
    if(math.random(0, 1)==0) then
        spell.ItemPotionEffect(player, "1:600:0")
    end
    if(math.random(0, 1)==0) then
        spell.ItemPotionEffect(player, "8:600:0")
    end
    if(math.random(0, 1)==0) then
        spell.ItemPotionEffect(player, "10:600:0")
    end
    if(math.random(0, 19)==0) then
        spell.ItemPotionEffect(player, "19:100:0")
    end
    
    return true
end

--Enchantments
function poisonous(player, target, block, location)
    spell.ItemPotionEffect(target, "19:400:1")
    
    return true
end

function freezing(player, target, block, location)
    spell.ItemPotionEffect(target, "2:200:7")
    spell.ItemPotionEffect(target, "8:200:128")
    spell.ItemPotionEffect(target, "4:200:7")
    
    return true
end