package com.example;

import com.authzed.api.v1.Core.RelationshipUpdate;

public enum RelationOperation {
  CREATE,
  TOUCH;

  public RelationshipUpdate.Operation getRelationOperation() {
    switch (this) {
      case CREATE:
        return RelationshipUpdate.Operation.OPERATION_CREATE;
      case TOUCH:
        return RelationshipUpdate.Operation.OPERATION_TOUCH;
      default:
        return null;
    }
  }
}
