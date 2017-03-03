package mobi.kujon.google_drive.ui.fragments.files.recycler_classes;

import android.support.v7.util.DiffUtil;
import android.util.Log;

import java.util.List;

import mobi.kujon.google_drive.model.dto.file.FileDTO;

public class FilesDiffCallback extends DiffUtil.Callback {

    private List<FileDTO> oldList;
    private List<FileDTO> newList;

    public FilesDiffCallback(List<FileDTO> oldList, List<FileDTO> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList != null ? oldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newList != null ? newList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        boolean b = newList.get(newItemPosition).getFileId().equals(oldList.get(oldItemPosition).getFileId());
        if (!b) {
            Log.d("ITEMS_SAME", "old" + String.valueOf(oldItemPosition) + " new" + String.valueOf(newItemPosition));
        }
        return b;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {

        boolean equals = newList.get(newItemPosition).equals(oldList.get(oldItemPosition));
        if (!equals) {
            Log.d("ITEMS_EQUALS", "old" + String.valueOf(oldItemPosition) + " new" + String.valueOf(newItemPosition));
        }
        return equals;
    }


}


