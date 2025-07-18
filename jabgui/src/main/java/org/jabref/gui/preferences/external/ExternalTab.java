package org.jabref.gui.preferences.external;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import org.jabref.gui.actions.ActionFactory;
import org.jabref.gui.actions.StandardActions;
import org.jabref.gui.help.HelpAction;
import org.jabref.gui.preferences.AbstractPreferenceTabView;
import org.jabref.gui.preferences.PreferencesTab;
import org.jabref.gui.push.GuiPushToApplication;
import org.jabref.gui.util.IconValidationDecorator;
import org.jabref.gui.util.ViewModelListCellFactory;
import org.jabref.logic.help.HelpFile;
import org.jabref.logic.l10n.Localization;

import com.airhacks.afterburner.views.ViewLoader;
import de.saxsys.mvvmfx.utils.validation.visualization.ControlsFxVisualizer;

public class ExternalTab extends AbstractPreferenceTabView<ExternalTabViewModel> implements PreferencesTab {

    @FXML private TextField eMailReferenceSubject;
    @FXML private CheckBox autoOpenAttachedFolders;
    @FXML private ComboBox<GuiPushToApplication> pushToApplicationCombo;
    @FXML private TextField citeCommand;
    @FXML private Button autolinkExternalHelp;

    @FXML private CheckBox useCustomTerminal;
    @FXML private TextField customTerminalCommand;
    @FXML private Button customTerminalBrowse;
    @FXML private CheckBox useCustomFileBrowser;
    @FXML private TextField customFileBrowserCommand;
    @FXML private Button customFileBrowserBrowse;
    @FXML private TextField kindleEmail;

    private final ControlsFxVisualizer validationVisualizer = new ControlsFxVisualizer();

    public ExternalTab() {
        ViewLoader.view(this)
                  .root(this)
                  .load();
    }

    @Override
    public String getTabName() {
        return Localization.lang("External programs");
    }

    public void initialize() {
        this.viewModel = new ExternalTabViewModel(dialogService, preferences);

        new ViewModelListCellFactory<GuiPushToApplication>()
                .withText(GuiPushToApplication::getDisplayName)
                .withIcon(GuiPushToApplication::getApplicationIcon)
                .install(pushToApplicationCombo);

        eMailReferenceSubject.textProperty().bindBidirectional(viewModel.eMailReferenceSubjectProperty());
        autoOpenAttachedFolders.selectedProperty().bindBidirectional(viewModel.autoOpenAttachedFoldersProperty());

        pushToApplicationCombo.itemsProperty().bind(viewModel.pushToApplicationsListProperty());
        pushToApplicationCombo.valueProperty().bindBidirectional(viewModel.selectedPushToApplication());
        citeCommand.textProperty().bindBidirectional(viewModel.citeCommandProperty());

        useCustomTerminal.selectedProperty().bindBidirectional(viewModel.useCustomTerminalProperty());
        customTerminalCommand.textProperty().bindBidirectional(viewModel.customTerminalCommandProperty());
        customTerminalCommand.disableProperty().bind(useCustomTerminal.selectedProperty().not());
        customTerminalBrowse.disableProperty().bind(useCustomTerminal.selectedProperty().not());

        useCustomFileBrowser.selectedProperty().bindBidirectional(viewModel.useCustomFileBrowserProperty());
        customFileBrowserCommand.textProperty().bindBidirectional(viewModel.customFileBrowserCommandProperty());
        customFileBrowserCommand.disableProperty().bind(useCustomFileBrowser.selectedProperty().not());
        customFileBrowserBrowse.disableProperty().bind(useCustomFileBrowser.selectedProperty().not());

        kindleEmail.textProperty().bindBidirectional(viewModel.kindleEmailProperty());

        validationVisualizer.setDecoration(new IconValidationDecorator());
        Platform.runLater(() -> {
            validationVisualizer.initVisualization(viewModel.terminalCommandValidationStatus(), customTerminalCommand);
            validationVisualizer.initVisualization(viewModel.fileBrowserCommandValidationStatus(), customFileBrowserCommand);
        });

        ActionFactory actionFactory = new ActionFactory();
        actionFactory.configureIconButton(StandardActions.HELP_PUSH_TO_APPLICATION, new HelpAction(HelpFile.PUSH_TO_APPLICATION, dialogService, preferences.getExternalApplicationsPreferences()), autolinkExternalHelp);
    }

    @FXML
    void pushToApplicationSettings() {
        viewModel.pushToApplicationSettings();
    }

    @FXML
    void useTerminalCommandBrowse() {
        viewModel.customTerminalBrowse();
    }

    @FXML
    void useFileBrowserSpecialCommandBrowse() {
        viewModel.customFileBrowserBrowse();
    }

    @FXML
    void resetCiteCommandToDefault() {
        viewModel.resetCiteCommandToDefault();
    }
}
