package mobi.kujon.google_drive.ui.dialogs.sort_strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mobi.kujon.google_drive.model.dto.file.FileDTO;

/**
 *
 */

public class AuthorSortStrategy  implements SortStrategy{
    @Override
    public List<FileDTO> sort(List<FileDTO> fileDTOs) {
        List<FileDTO> sorts = new ArrayList<>(fileDTOs);
        Collections.sort(sorts, (o1, o2) -> o1.getUserName().compareTo(o2.getUserName()));
        return sorts;
    }

    @Override
    public @SortStrategyType  int getSortStrategy() {
        return SortStrategyType.AUTHOR;
    }
}
