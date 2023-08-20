package codersafterdark.reskillable.advancement;

import com.google.common.collect.Maps;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.PlayerAdvancements;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;
import java.util.function.Function;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class AbstractCriterionTrigger<T extends CriterionListeners<U>, U extends CriterionTriggerInstance> implements CriterionTrigger<U> {
    private final ResourceLocation id;
    private final Function<PlayerAdvancements, T> createNew;
    private final Map<PlayerAdvancements, T> listeners = Maps.newHashMap();

    protected AbstractCriterionTrigger(ResourceLocation id, Function<PlayerAdvancements, T> createNew) {
        this.id = id;
        this.createNew = createNew;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public void addPlayerListener(PlayerAdvancements playerAdvancements, Listener<U> listener) {
        T listeners = this.listeners.get(playerAdvancements);
        if (listeners == null) {
            listeners = createNew.apply(playerAdvancements);
            this.listeners.put(playerAdvancements, listeners);
        }
        listeners.add(listener);
    }

    @Override
    public void removePlayerListener(PlayerAdvancements playerAdvancements, Listener<U> listener) {
        T listeners = this.listeners.get(playerAdvancements);

        if (listeners != null) {
            listeners.remove(listener);
            if (listeners.isEmpty()) {
                this.listeners.remove(playerAdvancements);
            }
        }
    }

    @Override
    public void removePlayerListeners(PlayerAdvancements playerAdvancements) {
        this.listeners.remove(playerAdvancements);
    }

    @Nullable
    public T getListeners(PlayerAdvancements playerAdvancements) {
        return this.listeners.get(playerAdvancements);
    }

}
