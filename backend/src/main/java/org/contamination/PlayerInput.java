package org.contamination;

public class PlayerInput {
  Boolean up = false;
  Boolean left = false;
  Boolean right = false;
  Boolean down = false;

  /**
   * Get player direction angle based on its input. If no input is given, return
   * -1
   */
  public static double getDirectionAngle(PlayerInput playerInput) {
    if (playerInput.right && playerInput.down) {
      return Math.PI / 4;
    } else if (playerInput.right && playerInput.up) {
      return 7 * Math.PI / 4;
    } else if (playerInput.left && playerInput.down) {
      return 3 * Math.PI / 4;
    } else if (playerInput.left && playerInput.up) {
      return 5 * Math.PI / 4;
    } else if (playerInput.right) {
      return 0;
    } else if (playerInput.down) {
      return Math.PI / 2;
    } else if (playerInput.left) {
      return Math.PI;
    } else if (playerInput.up) {
      return 3 * Math.PI / 2;
    } else {
      return -1;
    }
  }
}
