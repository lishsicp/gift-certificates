package com.epam.esm.dto.group;

import javax.validation.groups.Default;

/**
 * This interface is used as a marker interface for specifying validation rules that should
 * be applied when creating new entities using the javax validation framework.
 */
public interface OnPersist extends Default {
}
