package mobi.kujon.google_drive.ui.dialogs.sort_strategy;

import java.util.Collections;
import java.util.List;

import mobi.kujon.google_drive.model.dto.file.FileDTO;

/**
 *
 */

public class FileNameSortStrategy implements SortStrategy {
    @Override
    public List<FileDTO> sort(List<FileDTO> fileDTOs) {
        Collections.sort(fileDTOs, (o1, o2) -> o1.getFileName().toLowerCase().compareTo(o2.getFileName().toLowerCase()));
        return fileDTOs;
    }

    @Override
    public @SortStrategyType  int getSortStrategy() {
        return SortStrategyType.FILE_NAME;
    }
}
