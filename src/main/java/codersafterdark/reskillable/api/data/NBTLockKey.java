package codersafterdark.reskillable.api.data;

import net.minecraft.nbt.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class NBTLockKey implements FuzzyLockKey {
    protected CompoundTag tag;

    protected NBTLockKey(CompoundTag tag) {
        this.tag = tag == null || tag.isEmpty() ? null : tag;
    }

    protected static boolean similarNBT(Tag full, Tag partial) {
        if (full == null) {
            return partial == null;
        }
        if (partial == null) {
            return true;
        }
        if (full.getId() != partial.getId()) {
            return false;
        }
        if (full.equals(partial)) {
            return true;
        }
        switch (full.getId()) {
            case Tag.TAG_COMPOUND:
                CompoundTag fullTag = (CompoundTag) full;
                CompoundTag partialTag = (CompoundTag) partial;
                Set<String> ptKeys = partialTag.getAllKeys();
                for (String partialKey : ptKeys) {
                    //One of the keys is missing OR the tags are different types OR they do not match
                    if (!fullTag.contains(partialKey, partialTag.getTagType(partialKey)) || !similarNBT(fullTag.get(partialKey), partialTag.get(partialKey))) {
                        return false;
                    }
                }
                return true;
            case Tag.TAG_LIST:
                ListTag fTagList = (ListTag) full;
                ListTag pTagList = (ListTag) partial;
                if (fTagList.isEmpty() && !pTagList.isEmpty() || fTagList.getType() != pTagList.getType()) {
                    return false;
                }
                for (int i = 0; i < pTagList.size(); i++) {
                    Tag pTag = pTagList.get(i);
                    boolean hasTag = false;
                    for (int j = 0; j < fTagList.size(); j++) {
                        if (similarNBT(fTagList.get(j), pTag)) {
                            hasTag = true;
                            break;
                        }
                    }
                    if (!hasTag) {
                        return false;
                    }
                }
                return true;
            case Tag.TAG_BYTE_ARRAY:
                byte[] fByteArray = ((ByteArrayTag) full).getAsByteArray();
                byte[] pByteArray = ((ByteArrayTag) partial).getAsByteArray();
                List<Integer> hits = new ArrayList<>();
                for (byte pByte : pByteArray) {
                    boolean hasMatch = false;
                    for (int i = 0; i < fByteArray.length; i++) {
                        if (!hits.contains(i) && pByte == fByteArray[i]) {
                            hits.add(i);
                            hasMatch = true;
                            break;
                        }
                    }
                    if (!hasMatch) {
                        return false;
                    }
                }
                return true;
            case Tag.TAG_INT_ARRAY:
                int[] fIntArray = ((IntArrayTag) full).getAsIntArray();
                int[] pIntArray = ((IntArrayTag) partial).getAsIntArray();
                hits = new ArrayList<>();
                for (int pInt : pIntArray) {
                    boolean hasMatch = false;
                    for (int i = 0; i < fIntArray.length; i++) {
                        if (!hits.contains(i) && pInt == fIntArray[i]) {
                            hits.add(i);
                            hasMatch = true;
                            break;
                        }
                    }
                    if (!hasMatch) {
                        return false;
                    }
                }
                return true;
            case Tag.TAG_LONG_ARRAY:
                long[] fLongArray = getLongArray((LongArrayTag) full);
                long[] pLongArray = getLongArray((LongArrayTag) partial);
                hits = new ArrayList<>();
                for (long pLong : pLongArray) {
                    boolean hasMatch = false;
                    for (int i = 0; i < fLongArray.length; i++) {
                        if (!hits.contains(i) && pLong == fLongArray[i]) {
                            hits.add(i);
                            hasMatch = true;
                            break;
                        }
                    }
                    if (!hasMatch) {
                        return false;
                    }
                }
                return true;
            default:
                return false;
        }
    }

    private static long[] getLongArray(LongArrayTag tag) {
        String t = tag.toString();
        String[] entries = t.substring(3, t.length() - 1).split(",");//Trim the closing bracket and the [L; at the start
        long[] data = new long[entries.length];
        for (int i = 0; i < entries.length; i++) {
            try {
                data[i] = Long.parseLong(entries[i].substring(0, entries[i].length() - 1));//Trim the L
            } catch (Exception ignored) {
            }
        }
        return data;
    }

    public CompoundTag getTag() {
        return this.tag;
    }

    @Override
    public boolean isNotFuzzy() {
        return this.tag == null;
    }

    @Override
    public boolean fuzzyEquals(FuzzyLockKey other) {
        if (other == this) {
            return true;
        }
        if (other instanceof NBTLockKey) {
            return similarNBT(getTag(), ((NBTLockKey) other).getTag());
        }
        return false;
    }
}