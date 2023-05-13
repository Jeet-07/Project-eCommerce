package com.manjeet.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="categories")
public class Category implements Comparable<Category> {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Integer id;
    @Column(length=128,nullable=false,unique=true)
    String name;
    @Column(length=64,nullable=false,unique=true)
    String alias;
    @Column(length=64)
    String image;

    private boolean enabled;
    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="parent_id")
    private Category parent;

    @Column(name = "all_parent_ids", length = 256, nullable = true)
    private String allParentIDs;

    @OneToMany(mappedBy = "parent",fetch = FetchType.EAGER)
    @OrderBy("name asc")
    private Set<Category> children = new HashSet<>();

    @Transient
    private String hierarchicalName;

    public Category() {
        super();
    }

    public Category(Integer id) {
        this.id = id;
    }
    public Category(String name) {
        this.name = name;
    }

    @Transient
    public String getImagePath(){
        if(this.id==null || this.image == null)
            return "/images/image-default.png";

        return "/category-photos/"+this.id+"/"+this.image;
    }

    @Override
    @Transient
    public int compareTo(Category cat) {
        return (this.name).compareTo(cat.getName());
    }
    @Transient
    public boolean isHasChildren() {
        return this.children.size()>0;
    }

}
