package com.example;

import static org.junit.Assert.assertTrue;

import com.example.SpiceDb;
import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

  /**
   * Rigorous Test :-)
   */
  @Test
  public void shouldAnswerWithTrue() {
    try {
      SpiceDb.writeSchema("src/main/resources/resource_permission.yaml");
      // Write relationships
      String result = SpiceDb.manageObjectRelations(
        Relation.ADMINISTRATOR,
        RelationOperation.CREATE,
        ObjectType.CAA,
        "DGCA",
        ObjectType.USER,
        "dgca_user"
      );

      System.out.println(result);

      result =
        SpiceDb.manageObjectRelations(
          Relation.ADMINISTRATOR,
          RelationOperation.CREATE,
          ObjectType.MANUFACTURER,
          "man_1",
          ObjectType.USER,
          "man_1_user"
        );

      System.out.println(result);

      result =
        SpiceDb.manageObjectRelations(
          Relation.ADMINISTRATOR,
          RelationOperation.CREATE,
          ObjectType.MANUFACTURER,
          "man_1",
          ObjectType.USER,
          "man_1_user"
        );

      System.out.println(result);

      result =
        SpiceDb.manageObjectRelations(
          Relation.REGULATOR,
          RelationOperation.CREATE,
          ObjectType.UAS,
          "uas_1",
          ObjectType.CAA,
          "dgca"
        );

      System.out.println(result);

      result =
        SpiceDb.manageObjectRelations(
          Relation.MANUFACTURER,
          RelationOperation.CREATE,
          ObjectType.UAS,
          "uas_1",
          ObjectType.MANUFACTURER,
          "man_1"
        );

      System.out.println(result);

      Thread.sleep(5000);

      boolean permissionResult = SpiceDb.checkObjectPermission(
        Permission.COMMISION_UAS,
        ObjectType.MANUFACTURER,
        "man_1",
        ObjectType.USER,
        "man_1_user"
      );

      System.out.println(permissionResult);

      permissionResult =
        SpiceDb.checkObjectPermission(
          Permission.DECOMMISION_UAS,
          ObjectType.UAS,
          "uas_1",
          ObjectType.USER,
          "man_1_user"
        );

      System.out.println(permissionResult);

      permissionResult =
        SpiceDb.checkObjectPermission(
          Permission.COMMISION_UAS,
          ObjectType.CAA,
          "DGCA",
          ObjectType.USER,
          "dgca_user"
        );

      System.out.println(permissionResult);

      permissionResult =
        SpiceDb.checkObjectPermission(
          Permission.DECOMMISION_UAS,
          ObjectType.UAS,
          "uas_1",
          ObjectType.USER,
          "dgca_user"
        );

      System.out.println(permissionResult);

      permissionResult =
        SpiceDb.checkObjectPermission(
          Permission.DECOMMISION_UAS,
          ObjectType.UAS,
          "uas_1",
          ObjectType.USER,
          "man_user_1"
        );

      System.out.println(permissionResult);
    } catch (InterruptedException exception) {
      // Uh oh!
    } finally {
      SpiceDb.shutdownChannel();
    }
  }
}
