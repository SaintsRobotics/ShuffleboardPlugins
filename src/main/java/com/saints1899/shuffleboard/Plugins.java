package com.saints1899.shuffleboard;

import edu.wpi.first.shuffleboard.api.plugin.Description;
import edu.wpi.first.shuffleboard.api.plugin.Plugin;
import edu.wpi.first.shuffleboard.api.widget.ComponentType;
import edu.wpi.first.shuffleboard.api.widget.WidgetType;

import java.util.List;

import com.saints1899.shuffleboard.largetext.LargeTextWidget;
import com.saints1899.shuffleboard.matchSim.MatchSimController;

@Description(group = "Saints 1899", name = "Plugins", summary = "Provides plugins developed by the Saints robotics team", version = "0.1.0")
public class Plugins extends Plugin {
    @Override
    public List<ComponentType> getComponents() {
        return List.of(WidgetType.forAnnotatedWidget(LargeTextWidget.class),
                WidgetType.forAnnotatedWidget(MatchSimController.class));
    }
}
