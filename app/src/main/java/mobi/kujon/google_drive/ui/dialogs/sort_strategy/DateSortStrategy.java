package mobi.kujon.google_drive.ui.dialogs.sort_strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.kujon.google_drive.model.dto.file.FileDTO;

/**
 *
 */

public class DateSortStrategy implements SortStrategy {
    @Override
    public List<FileDTO> sort(List<FileDTO> fileDTOs) {
        List<FileDTO> sorts = new ArrayList<>(fileDTOs);
        Collections.sort(sorts, (o1, o2) -> o2.getDate().compareTo(o1.getDate()));
        return sorts;
    }

    @Override
    public @SortStrategyType  int getSortStrategy() {
        return SortStrategyType.DATE;
    }
}
