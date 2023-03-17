package gg.rsmod.plugins.content.areas.edgeville

/**
 * @author Alycia <https://github.com/alycii>
 */
val OPEN_DOOR_SFX = 62
val CLOSE_DOOR_SFX = 60

on_obj_option(obj = Objs.DOOR_1804, option = "open") {
    if(!player.inventory.contains(Items.BRASS_KEY)) {
        player.message("The door is locked.")
        return@on_obj_option
    }

    handleDoor(player)
}

on_obj_option(obj = Objs.LADDER_12389, option = "Climb-Down") {
    player.handleBasicLadder(climbUp = false)
}

on_obj_option(obj = Objs.LADDER_29358, option = "Climb-Down") {
    player.handleBasicLadder(climbUp = false)
}


fun handleDoor(player: Player) {
    val closedDoor = DynamicObject(id = 1804, type = 0, rot = 1, tile = Tile(x = 3115, z = 3449))
    player.lock = LockState.DELAY_ACTIONS
    world.remove(closedDoor)
    val door = DynamicObject(id = 1803, type = 0, rot = 1, tile = Tile(x = 3115, z = 3449))
    player.playSound(id = OPEN_DOOR_SFX)
    world.spawn(door)

    player.queue {
        val x = 3115
        val z = if (player.tile.z == 3450) 3449 else 3450
        player.walkTo(tile = Tile(x = x, z = z), detectCollision = false)
        wait(3)
        world.remove(door)
        player.lock = LockState.NONE
        world.spawn(closedDoor)
        player.playSound(CLOSE_DOOR_SFX)
    }
}