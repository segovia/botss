/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.9
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package com.google.fpl.liquidfun;

public class ParticleSystemDef {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected ParticleSystemDef(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(ParticleSystemDef obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        liquidfunJNI.delete_ParticleSystemDef(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public ParticleSystemDef() {
    this(liquidfunJNI.new_ParticleSystemDef(), true);
  }

  public void setRadius(float value) {
    liquidfunJNI.ParticleSystemDef_radius_set(swigCPtr, this, value);
  }

  public float getRadius() {
    return liquidfunJNI.ParticleSystemDef_radius_get(swigCPtr, this);
  }

  public void setPressureStrength(float value) {
    liquidfunJNI.ParticleSystemDef_pressureStrength_set(swigCPtr, this, value);
  }

  public float getPressureStrength() {
    return liquidfunJNI.ParticleSystemDef_pressureStrength_get(swigCPtr, this);
  }

  public void setDampingStrength(float value) {
    liquidfunJNI.ParticleSystemDef_dampingStrength_set(swigCPtr, this, value);
  }

  public float getDampingStrength() {
    return liquidfunJNI.ParticleSystemDef_dampingStrength_get(swigCPtr, this);
  }

  public void setElasticStrength(float value) {
    liquidfunJNI.ParticleSystemDef_elasticStrength_set(swigCPtr, this, value);
  }

  public float getElasticStrength() {
    return liquidfunJNI.ParticleSystemDef_elasticStrength_get(swigCPtr, this);
  }

  public void setSpringStrength(float value) {
    liquidfunJNI.ParticleSystemDef_springStrength_set(swigCPtr, this, value);
  }

  public float getSpringStrength() {
    return liquidfunJNI.ParticleSystemDef_springStrength_get(swigCPtr, this);
  }

  public void setViscousStrength(float value) {
    liquidfunJNI.ParticleSystemDef_viscousStrength_set(swigCPtr, this, value);
  }

  public float getViscousStrength() {
    return liquidfunJNI.ParticleSystemDef_viscousStrength_get(swigCPtr, this);
  }

  public void setSurfaceTensionPressureStrength(float value) {
    liquidfunJNI.ParticleSystemDef_surfaceTensionPressureStrength_set(swigCPtr, this, value);
  }

  public float getSurfaceTensionPressureStrength() {
    return liquidfunJNI.ParticleSystemDef_surfaceTensionPressureStrength_get(swigCPtr, this);
  }

  public void setSurfaceTensionNormalStrength(float value) {
    liquidfunJNI.ParticleSystemDef_surfaceTensionNormalStrength_set(swigCPtr, this, value);
  }

  public float getSurfaceTensionNormalStrength() {
    return liquidfunJNI.ParticleSystemDef_surfaceTensionNormalStrength_get(swigCPtr, this);
  }

  public void setRepulsiveStrength(float value) {
    liquidfunJNI.ParticleSystemDef_repulsiveStrength_set(swigCPtr, this, value);
  }

  public float getRepulsiveStrength() {
    return liquidfunJNI.ParticleSystemDef_repulsiveStrength_get(swigCPtr, this);
  }

  public void setPowderStrength(float value) {
    liquidfunJNI.ParticleSystemDef_powderStrength_set(swigCPtr, this, value);
  }

  public float getPowderStrength() {
    return liquidfunJNI.ParticleSystemDef_powderStrength_get(swigCPtr, this);
  }

  public void setEjectionStrength(float value) {
    liquidfunJNI.ParticleSystemDef_ejectionStrength_set(swigCPtr, this, value);
  }

  public float getEjectionStrength() {
    return liquidfunJNI.ParticleSystemDef_ejectionStrength_get(swigCPtr, this);
  }

  public void setStaticPressureStrength(float value) {
    liquidfunJNI.ParticleSystemDef_staticPressureStrength_set(swigCPtr, this, value);
  }

  public float getStaticPressureStrength() {
    return liquidfunJNI.ParticleSystemDef_staticPressureStrength_get(swigCPtr, this);
  }

  public void setStaticPressureRelaxation(float value) {
    liquidfunJNI.ParticleSystemDef_staticPressureRelaxation_set(swigCPtr, this, value);
  }

  public float getStaticPressureRelaxation() {
    return liquidfunJNI.ParticleSystemDef_staticPressureRelaxation_get(swigCPtr, this);
  }

  public void setStaticPressureIterations(int value) {
    liquidfunJNI.ParticleSystemDef_staticPressureIterations_set(swigCPtr, this, value);
  }

  public int getStaticPressureIterations() {
    return liquidfunJNI.ParticleSystemDef_staticPressureIterations_get(swigCPtr, this);
  }

  public void setColorMixingStrength(float value) {
    liquidfunJNI.ParticleSystemDef_colorMixingStrength_set(swigCPtr, this, value);
  }

  public float getColorMixingStrength() {
    return liquidfunJNI.ParticleSystemDef_colorMixingStrength_get(swigCPtr, this);
  }

  public void setDestroyByAge(boolean value) {
    liquidfunJNI.ParticleSystemDef_destroyByAge_set(swigCPtr, this, value);
  }

  public boolean getDestroyByAge() {
    return liquidfunJNI.ParticleSystemDef_destroyByAge_get(swigCPtr, this);
  }

  public void setLifetimeGranularity(float value) {
    liquidfunJNI.ParticleSystemDef_lifetimeGranularity_set(swigCPtr, this, value);
  }

  public float getLifetimeGranularity() {
    return liquidfunJNI.ParticleSystemDef_lifetimeGranularity_get(swigCPtr, this);
  }

}
