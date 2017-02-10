package mobi.kujon.google_drive.ui.dialogs.sort_strategy;

import java.util.List;

import mobi.kujon.google_drive.model.dto.file.FileDTO;

/**
 *
 */

public interface SortStrategy {

    List<FileDTO> sort(List<FileDTO> fileDTOs);
    @SortStrategyType int getSortStrategy();
}
