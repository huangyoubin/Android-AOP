package com.binzi.plugin

import org.gradle.api.Project;

/**
 * @title:
 * @author: huangyoubin
 * @description:
 * @version:
 * @created: 2017-02-26 17:37
 */
public class AOPExtention {
    boolean aspectjOn = false
    boolean patchOn = false

    public AOPExtention(Project project) {
        aspectjOn = project.aop.aspectjOn
        patchOn = project.aop.patchOn
    }
}
