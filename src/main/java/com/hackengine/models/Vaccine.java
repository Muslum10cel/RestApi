/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hackengine.models;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author muslumoncel
 */
@XmlRootElement
public class Vaccine {

    private String vaccine_name;
    private int begin, last;

    public Vaccine(String vaccine_name) {
        this.vaccine_name = vaccine_name;
    }

    public Vaccine(String vaccine_name, int begin, int last) {
        this.vaccine_name = vaccine_name;
        this.begin = begin;
        this.last = last;
    }

    public Vaccine() {
    }

    public int getBegin() {
        return begin;
    }

    public int getLast() {
        return last;
    }

    public String getVaccine_name() {
        return vaccine_name;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public void setLast(int last) {
        this.last = last;
    }

    public void setVaccine_name(String vaccine_name) {
        this.vaccine_name = vaccine_name;
    }
}
