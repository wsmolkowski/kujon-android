package mobi.kujon.google_drive.ui.dialogs.sort_strategy;

import java.util.Collections;
import java.util.List;

import mobi.kujon.google_drive.model.dto.file.FileDTO;

/**
 *
 */

public class DateSortStrategy implements SortStrategy {
    @Override
    public List<FileDTO> sort(List<FileDTO> fileDTOs) {
        Collections.sort(fileDTOs, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));
        return fileDTOs;
    }

    @Override
    public @SortStrategyType  int getSortStrategy() {
        return SortStrategyType.DATE;
    }
}
