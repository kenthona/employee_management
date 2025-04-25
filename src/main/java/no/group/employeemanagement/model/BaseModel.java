package no.group.employeemanagement.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Setter
@Getter
@MappedSuperclass
@Data
public class BaseModel implements Serializable {

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @Column(
            name = "CREATED_DATE",
            nullable = false
    )
    private Date createdDate;
    @Column(
            name = "CREATED_BY",
            length = 50,
            nullable = true
    )
    private String createdBy;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    @Column(
            name = "MODIFIED_DATE",
            nullable = true
    )
    private Date modifiedDate;
    @Column(
            name = "MODIFIED_BY",
            length = 50,
            nullable = true
    )
    private String modifiedBy;

    @Column(
            name = "IS_DELETED",
            nullable = false,
            columnDefinition = "boolean default false"
    )
    private Boolean isDeleted = false;

    @PrePersist
    public void onCreate() {
        if (null == this.createdDate) {
            this.createdDate = new Date();
        }

    }

    @PreUpdate
    public void onUpdate() {
        if (null == this.modifiedDate) {
            this.modifiedDate = new Date();
        }

    }

    protected BaseModel(final BaseModelBuilder<?, ?> b) {
        this.createdDate = b.createdDate;
        this.createdBy = b.createdBy;
        this.modifiedDate = b.modifiedDate;
        this.modifiedBy = b.modifiedBy;
        this.isDeleted = b.isDeleted;
    }

    public static BaseModelBuilder<?, ?> builder() {
        return new BaseModelBuilderImpl();
    }

    public BaseModel(final Date createdDate, final String createdBy, final Date modifiedDate, final String modifiedBy, final Boolean isDeleted) {
        this.createdDate = createdDate;
        this.createdBy = createdBy;
        this.modifiedDate = modifiedDate;
        this.modifiedBy = modifiedBy;
        this.isDeleted = isDeleted;
    }

    public BaseModel() {
    }

    public abstract static class BaseModelBuilder<C extends BaseModel, B extends BaseModelBuilder<C, B>> {
        private Date createdDate;
        private String createdBy;
        private Date modifiedDate;
        private String modifiedBy;
        private Boolean isDeleted;

        public BaseModelBuilder() {
        }

        protected abstract B self();

        public abstract C build();

        public B createdDate(final Date createdDate) {
            this.createdDate = createdDate;
            return this.self();
        }

        public B createdBy(final String createdBy) {
            this.createdBy = createdBy;
            return this.self();
        }

        public B modifiedDate(final Date modifiedDate) {
            this.modifiedDate = modifiedDate;
            return this.self();
        }

        public B modifiedBy(final String modifiedBy) {
            this.modifiedBy = modifiedBy;
            return this.self();
        }

        public B isDeleted(final Boolean isDeleted) {
            this.isDeleted = isDeleted;
            return this.self();
        }

        public String toString() {
            return "BaseModel.BaseModelBuilder(createdDate=" + this.createdDate + ", createdBy=" + this.createdBy + ", modifiedDate=" + this.modifiedDate + ", modifiedBy=" + this.modifiedBy + ", isDeleted=" + this.isDeleted + ")";
        }
    }

    private static final class BaseModelBuilderImpl extends BaseModelBuilder<BaseModel, BaseModelBuilderImpl> {
        private BaseModelBuilderImpl() {
        }

        protected BaseModelBuilderImpl self() {
            return this;
        }

        public BaseModel build() {
            return new BaseModel(this);
        }
    }
}

