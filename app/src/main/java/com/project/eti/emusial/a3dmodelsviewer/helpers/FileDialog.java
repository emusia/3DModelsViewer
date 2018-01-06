package com.project.eti.emusial.a3dmodelsviewer.helpers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by emusial on 1/6/18.
 */

public class FileDialog {

    private static final String PARENT_DIR = "..";
    private final String TAG = getClass().getName();
    private String[] fileList;
    private File currentPath;

    public interface SelectedFileListener {
        void selectedFile(File file);
    }
    public interface SelectedDirectoryListener {
        void selectedDirectory(File directory);
    }

    private Listeners<SelectedFileListener> fileListeners = new Listeners<FileDialog.SelectedFileListener>();
    private Listeners<SelectedDirectoryListener> dirListeners = new Listeners<FileDialog.SelectedDirectoryListener>();
    private final Activity activity;
    private boolean selectDirectoryOption;
    private String fileEndsWith;

    public FileDialog(Activity activity, File initialPath) {
        this(activity, initialPath, null);
    }

    public FileDialog(Activity activity, File initialPath, String fileEndsWith) {
        this.activity = activity;
        setFileEndsWith(fileEndsWith);
        if (!initialPath.exists()) initialPath = Environment.getExternalStorageDirectory();
        loadFileList(initialPath);
    }

    public Dialog createFileDialog() {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(currentPath.getPath());
        if (selectDirectoryOption) {
            builder.setPositiveButton("Select directory", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Log.d(TAG, currentPath.getPath());
                    fDirectorySelectedEvent(currentPath);
                }
            });
        }

        builder.setItems(fileList, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String fileChosen = fileList[which];
                File chosenFile = getChosenFile(fileChosen);
                if (chosenFile.isDirectory()) {
                    loadFileList(chosenFile);
                    dialog.cancel();
                    dialog.dismiss();
                    showDialog();
                } else fFileSelectedEvent(chosenFile);
            }
        });

        dialog = builder.show();
        return dialog;
    }


    public void addFileListener(SelectedFileListener listener) {
        fileListeners.add(listener);
    }

    public void showDialog() {
        createFileDialog().show();
    }

    private void fFileSelectedEvent(final File file) {
        fileListeners.fEvent(new Listeners.FHandler<SelectedFileListener>() {
            public void fEvent(SelectedFileListener listener) {
                listener.selectedFile(file);
            }
        });
    }

    private void fDirectorySelectedEvent(final File directory) {
        dirListeners.fEvent(new Listeners.FHandler<SelectedDirectoryListener>() {
            public void fEvent(SelectedDirectoryListener listener) {
                listener.selectedDirectory(directory);
            }
        });
    }

    private void loadFileList(File path) {
        this.currentPath = path;
        List<String> r = new ArrayList<String>();
        if (path.exists()) {
            if (path.getParentFile() != null) r.add(PARENT_DIR);
            FilenameFilter filter = new FilenameFilter() {
                public boolean accept(File dir, String filename) {
                    File sel = new File(dir, filename);
                    if (!sel.canRead()) return false;
                    if (selectDirectoryOption) return sel.isDirectory();
                    else {
                        boolean endsWith = fileEndsWith == null || filename.toLowerCase().endsWith(fileEndsWith);
                        return endsWith || sel.isDirectory();
                    }
                }
            };
            String[] fileList1 = path.list(filter);
            for (String file : fileList1) {
                r.add(file);
            }
        }
        fileList = r.toArray(new String[]{});
    }

    private File getChosenFile(String fileChosen) {
        if (fileChosen.equals(PARENT_DIR)) return currentPath.getParentFile();
        else return new File(currentPath, fileChosen);
    }

    private void setFileEndsWith(String fileEndsWith) {
        this.fileEndsWith = fileEndsWith != null ? fileEndsWith.toLowerCase() : fileEndsWith;
    }
}

class Listeners<L> {
    private List<L> listeners = new ArrayList<L>();

    public interface FHandler<L> {
        void fEvent(L listener);
    }

    public void add(L listener) {
        listeners.add(listener);
    }

    public void fEvent(FHandler<L> fHandler) {
        List<L> copy = new ArrayList<L>(listeners);
        for (L l : copy) {
            fHandler.fEvent(l);
        }
    }

}

