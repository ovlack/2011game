package gg.rsmod.plugins.content.starter

import gg.rsmod.game.model.attr.CREATION_DATE
import gg.rsmod.game.model.attr.NEW_ACCOUNT_ATTR
import gg.rsmod.plugins.content.inter.bank.Bank

load_metadata {
    propertyFileName = "starter_kit"

    author = "Tomm"
    name = "Starter Kit"
    description = "Give items to new accounts."

    properties(
        // Inventory first row
        0.getItem to Items.BRONZE_HATCHET,
        1.getItem to Items.TINDERBOX_590,
        2.getItem to Items.SMALL_FISHING_NET,
        3.getItem to Items.SHRIMPS,

        // Inventory second row
        4.getItem to Items.BUCKET,
        5.getItem to Items.EMPTY_POT,
        6.getItem to Items.BREAD,
        7.getItem to Items.BRONZE_PICKAXE,

        // Inventory third row
        8.getItem to Items.BRONZE_DAGGER,
        9.getItem to Items.BRONZE_SWORD,
        10.getItem to Items.WOODEN_SHIELD,
        11.getItem to Items.SHORTBOW,

        // Inventory fourth row
        12.getItem to Items.BRONZE_ARROW,
        12.getItemAmount to 25,
        13.getItem to Items.AIR_RUNE,
        13.getItemAmount to 25,
        14.getItem to Items.MIND_RUNE,
        14.getItemAmount to 15,
        15.getItem to Items.WATER_RUNE,
        15.getItemAmount to 6,

        // Inventory sixth row
        16.getItem to Items.EARTH_RUNE,
        16.getItemAmount to 4,
        17.getItem to Items.BODY_RUNE,
        17.getItemAmount to 2,

        0.getBankItem to Items.COINS_995,
        0.getBankItemAmount to 25
    )
}

on_login {
    val newAccount = player.attr[NEW_ACCOUNT_ATTR] ?: return@on_login
    if (newAccount) {
        val inventory = player.getInventoryStarterItems()
        val bank = player.getBankStarterItems()

        inventory.forEach { slotItem ->
            player.inventory.add(item = slotItem.item, beginSlot = slotItem.slot)
        }

        bank.forEach { slotItem ->
            player.bank.add(item = slotItem.item, beginSlot = slotItem.slot)
        }

        player.setVarp(Bank.LAST_X_INPUT, 50)
        player.attr[CREATION_DATE] = System.currentTimeMillis()
    }
}

fun Player.getInventoryStarterItems() = getStarterItems(inventory.capacity, { getItem }, { getItemAmount })

fun Player.getBankStarterItems() = getStarterItems(bank.capacity, { getBankItem }, { getBankItemAmount })

fun getStarterItems(containerCapacity: Int, itemProperty: (Int).() -> String, amountProperty: (Int).() -> String): List<SlotItem> {
    val items = mutableListOf<SlotItem>()
    for (i in 0 until containerCapacity) {
        val item = getProperty<Int>(itemProperty(i)) ?: continue
        val amt = getProperty<Int>(amountProperty(i)) ?: 1
        items.add(SlotItem(i, Item(item, amt)))
    }
    return items
}

val Int.getItem: String
    get() = "item[$this]"

val Int.getItemAmount: String
    get() = "amount[$this]"

val Int.getBankItem: String
    get() = "bank_item[$this]"

val Int.getBankItemAmount: String
    get() = "bank_amount[$this]"