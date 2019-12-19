package com.odakota.tms.system.service.storage;

import com.odakota.tms.enums.StorageItemType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.file.Paths;

/**
 * Class that retains information such as folders and files in the storage
 *
 * @author haidv
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public class StorageItem {

    private StorageItemType type;

    private String path;

    private StorageItemMetadata metadata;

    private byte[] content;

    public StorageItem(StorageItemType type, String path, StorageItemMetadata metadata) {
        this.type = type;
        this.path = path;
        this.metadata = metadata;
    }

    /**
     * get the name of the storage item
     *
     * @return name
     */
    public String getName() {
        return Paths.get(path).getFileName().toString();
    }


    /**
     * Check if the storage item holds the contents of the file
     *
     * @return boolean
     */
    public boolean hasContent() {
        return (type == StorageItemType.FILE) && (content != null);
    }
}
