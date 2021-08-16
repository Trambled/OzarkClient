package me.trambled.ozark.ozarkclient.util.player.social;

import net.minecraft.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static me.trambled.ozark.ozarkclient.util.misc.WrapperUtil.mc;

public class OnlineFriendsUtil {

    public static List<Entity> entities = new ArrayList <> ( );

    static public List<Entity> getFriends() {
        entities.clear();
        entities.addAll(mc.world.playerEntities.stream().filter(entityPlayer -> FriendUtil.isFriend(entityPlayer.getName())).collect(Collectors.toList()));
     
        return entities;
    }
       
}