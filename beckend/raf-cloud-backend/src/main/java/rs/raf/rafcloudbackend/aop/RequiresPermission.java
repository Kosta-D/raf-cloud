package rs.raf.rafcloudbackend.aop;


import rs.raf.rafcloudbackend.model.Permission;

import java.lang.annotation.*;

import rs.raf.rafcloudbackend.model.Permission;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresPermission {
  Permission value();
}


