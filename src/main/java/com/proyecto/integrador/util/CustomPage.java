package com.proyecto.integrador.util;

import lombok.Generated;
import org.springframework.data.domain.Page;

import java.util.List;

public class CustomPage<T> {
    List<T> data;
    CustomPage.CustomPageable pageable;

    public CustomPage() {
    }

    public CustomPage(Page<T> page) {
        this.data = page.getContent();
        this.pageable = new CustomPageable(page.getPageable().getPageNumber() + 1,
            page.getPageable().getPageSize(), page.getTotalElements());
    }

    @Generated
    public List<T> getData() {
        return this.data;
    }

    @Generated
    public CustomPage.CustomPageable getPageable() {
        return this.pageable;
    }

    @Generated
    public void setData(final List<T> data) {
        this.data = data;
    }

    @Generated
    public void setPageable(final CustomPage.CustomPageable pageable) {
        this.pageable = pageable;
    }

    @Generated
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof CustomPage)) {
            return false;
        } else {
            CustomPage<?> other = (CustomPage) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                Object this$data = this.getData();
                Object other$data = other.getData();
                if (this$data == null) {
                    if (other$data != null) {
                        return false;
                    }
                } else if (!this$data.equals(other$data)) {
                    return false;
                }

                Object this$pageable = this.getPageable();
                Object other$pageable = other.getPageable();
                if (this$pageable == null) {
                    if (other$pageable != null) {
                        return false;
                    }
                } else if (!this$pageable.equals(other$pageable)) {
                    return false;
                }

                return true;
            }
        }
    }

    @Generated
    protected boolean canEqual(final Object other) {
        return other instanceof CustomPage;
    }

    @Generated
    public int hashCode() {
        int result = 1;
        Object $data = this.getData();
        result = result * 59 + ($data == null ? 43 : $data.hashCode());
        Object $pageable = this.getPageable();
        result = result * 59 + ($pageable == null ? 43 : $pageable.hashCode());
        return result;
    }

    @Generated
    public String toString() {
        List var10000 = this.getData();
        return "CustomPage(data=" + var10000 + ", pageable=" + this.getPageable() + ")";
    }

    static class CustomPageable {
        int pageNumber;
        int pageSize;
        long totalElements;

        public CustomPageable() {
        }

        public CustomPageable(int pageNumber, int pageSize, long totalElements) {
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
            this.totalElements = totalElements;
        }

        @Generated
        public int getPageNumber() {
            return this.pageNumber;
        }

        @Generated
        public int getPageSize() {
            return this.pageSize;
        }

        @Generated
        public long getTotalElements() {
            return this.totalElements;
        }

        @Generated
        public void setPageNumber(final int pageNumber) {
            this.pageNumber = pageNumber;
        }

        @Generated
        public void setPageSize(final int pageSize) {
            this.pageSize = pageSize;
        }

        @Generated
        public void setTotalElements(final long totalElements) {
            this.totalElements = totalElements;
        }

        @Generated
        public boolean equals(final Object o) {
            if (o == this) {
                return true;
            } else if (!(o instanceof CustomPageable)) {
                return false;
            } else {
                CustomPage.CustomPageable other = (CustomPageable) o;
                if (!other.canEqual(this)) {
                    return false;
                } else if (this.getPageNumber() != other.getPageNumber()) {
                    return false;
                } else if (this.getPageSize() != other.getPageSize()) {
                    return false;
                } else {
                    return this.getTotalElements() == other.getTotalElements();
                }
            }
        }

        @Generated
        protected boolean canEqual(final Object other) {
            return other instanceof CustomPageable;
        }

        @Generated
        public int hashCode() {
            int result = 1;
            result = result * 59 + this.getPageNumber();
            result = result * 59 + this.getPageSize();
            long $totalElements = this.getTotalElements();
            result = result * 59 + (int)($totalElements >>> 32 ^ $totalElements);
            return result;
        }

        @Generated
        public String toString() {
            int var10000 = this.getPageNumber();
            return "CustomPage.CustomPageable(pageNumber=" + var10000 + ", pageSize=" + this.getPageSize() + ", totalElements=" + this.getTotalElements() + ")";
        }
    }
}
