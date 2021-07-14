package me.trambled.ozark.ozarkclient.command.commands;


import me.trambled.ozark.Ozark;
import me.trambled.ozark.ozarkclient.command.Command;
import me.trambled.ozark.ozarkclient.util.MessageUtil;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ConfigBackupCommand extends Command {
    public ConfigBackupCommand() {
        super("backup", "backup ur config (also can be use to instantly zip ur confiig)");
    }

    public boolean get_message(String[] message) {
        String filename = "ozark-cofig-backup-" + Ozark.get_version() + "-" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".zip";
        zip(new File(Ozark.get_config_manager().FILE_DIRECTORY.getPath()), new File(filename));
        MessageUtil.send_client_message("Config successfully saved in " + filename + "!");
        MessageUtil.send_client_message("Your backup config is ready at .minecraft folder.");
        return true;}


    public static void zip(File source, File dest) {
        List<String> list = new ArrayList<String>();
        createFileList(source, source, list);
        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(dest));
            for (String file : list) {
                ZipEntry ze = new ZipEntry(file);
                FileInputStream in = new FileInputStream(file);
                byte buffer[] = new byte[1024];
                zos.putNextEntry(ze);
                while (true) {
                    int len = in.read(buffer);
                    if (len <= 0) break;
                    zos.write(buffer, 0, len);
                }
                in.close();
                zos.closeEntry();
            }
            zos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createFileList(File file, File source, List<String> list) {
        if (file.isFile()) {
            list.add(file.getPath());
        } else if (file.isDirectory()) {
            for (String subfile : file.list()) {
                createFileList(new File(file, subfile), source, list);
            }
        }
    }
}