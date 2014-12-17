package com.mapr.demo.recsys.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.solr.client.solrj.beans.Field;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.solr.core.mapping.SolrDocument;

@Document(indexName = "foo")
@SolrDocument
public class Item {
    @Id
    @Field
    private String id;

    @Field("title")
    @org.springframework.data.elasticsearch.annotations.Field
    @JsonProperty("title")
    private String name;

    public Item() {
    }

    public Item(String id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Item [id=" + this.id + ", name=" + this.name + "]";
    }

}
