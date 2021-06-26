package me.trambled.ozark.ozarkclient.module.movement;

import me.trambled.ozark.ozarkclient.module.Category;
import me.trambled.ozark.ozarkclient.module.Module;
import me.trambled.ozark.ozarkclient.module.Setting;

public class Sprint extends Module {
    
    public Sprint() {
        super(Category.MOVEMENT);

		this.name        = "Sprint";
		this.tag         = "Sprint";
		this.description = "ZOOOOOOOOM.";
    }

    Setting rage = create("Rage", "SprintRage", true);

    @Override
	public void update() {
    	if (mc.player == null) return;

    	if (rage.get_value(true) && (mc.player.moveForward != 0 || mc.player.moveStrafing != 0)) {
			mc.player.setSprinting(true);
		} else mc.player.setSprinting(mc.player.moveForward > 0 || mc.player.moveStrafing > 0);
	}
	@Override
	public String array_detail() {
		if (rage.get_value(true )) {
			return "Rage";
		}else{
			return "Legit";

		}
	}}