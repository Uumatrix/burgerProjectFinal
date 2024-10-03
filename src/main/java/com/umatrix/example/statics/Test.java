package com.umatrix.example.statics;

public enum Test {
}
/*
Create:

DataIntegrityViolationException for unique constraint violations.
ConstraintViolationException for invalid inputs.
Read:

EntityNotFoundException if the entity is not found.
Update:

OptimisticLockException if there’s a versioning issue (in case of versioned entities).
EntityNotFoundException if attempting to update a non-existing entity.
Delete:

EmptyResultDataAccessException if trying to delete a non-existing entity.
DataIntegrityViolationException if deleting would violate a foreign key constraint.
 */