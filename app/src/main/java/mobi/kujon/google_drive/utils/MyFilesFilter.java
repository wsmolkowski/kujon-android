package mobi.kujon.google_drive.utils;

import com.github.underscore.$;

import java.util.List;

import mobi.kujon.google_drive.model.KujonFile;
import mobi.kujon.google_drive.mvp.files_list.FilesOwnerType;

/**
 *
 */

public class MyFilesFilter {

    public List<KujonFile> filterFiles(List<KujonFile> kujonFiles,@FilesOwnerType int condition){
       return  $.filter(kujonFiles, arg -> {
            switch (condition){
                case FilesOwnerType.MY:
                    return arg.myFile;
                default:
                    return !arg.myFile;
            }
        });
    }
}
