package vazkii.skillable.skill;

import net.minecraft.init.Blocks;
import vazkii.skillable.skill.magic.TraitGoldenOsmosis;

public class SkillMagic extends Skill {

	public SkillMagic() {
		super("magic", 7, Blocks.END_STONE);
	}

	@Override
	public void initUnlockables() {
		addUnlockable(new TraitGoldenOsmosis());
	}
	
}
