package io.github.darkkronicle.betterblockoutline.config.gui;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.util.StringUtils;
import io.github.darkkronicle.betterblockoutline.config.ConfigStorage;
import io.github.darkkronicle.betterblockoutline.config.SaveableConfig;
import lombok.Getter;
import net.minecraft.client.gui.screen.Screen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public enum GuiTab {
    GENERAL("general", wrapSaveableOptions(ConfigStorage.General.OPTIONS)),
    OUTLINE_MODS("outline_mods", wrapScreen((parent) -> new ColorModifierListScreen(ColorModifierListScreen.Type.OUTLINE))),
    FILL_MODS("fill_mods", wrapScreen((parent) -> new ColorModifierListScreen(ColorModifierListScreen.Type.FILL)))
    ;

    @Getter
    private final TabSupplier tabSupplier;
    @Getter
    private final String configValue;

    GuiTab(String configValue, TabSupplier tabSupplier) {
        this.configValue = configValue;
        this.tabSupplier = tabSupplier;
    }

    public String getDisplayName() {
        return StringUtils.translate("betterblockoutline.config.tab." + configValue);
    }

    public static TabSupplier wrapSaveableOptions(List<SaveableConfig<? extends IConfigBase>> options) {
        List<IConfigBase> config = new ArrayList<>();
        for (SaveableConfig<? extends IConfigBase> s : options) {
            config.add(s.config);
        }
        return wrapOptions(config);
    }

    public static TabSupplier wrapOptions(List<IConfigBase> configs) {
        return wrapOptions(() -> configs);
    }

    public static TabSupplier wrapOptions(Supplier<List<IConfigBase>> options) {
        return new TabSupplier() {
            @Override
            public List<IConfigBase> getOptions() {
                return options.get();
            }
        };
    }

    public static TabSupplier wrapScreen(Function<Screen, Screen> screenSupplier) {
        return new TabSupplier() {
            @Override
            public Screen getScreen(Screen parent) {
                return screenSupplier.apply(parent);
            }
        };
    }

    public interface TabSupplier {

        default List<IConfigBase> getOptions() {
            return null;
        }

        default Screen getScreen(Screen parent) {
            return null;
        }

    }

}