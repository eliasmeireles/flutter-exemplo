import 'package:flutter/material.dart';

class DeleteDismissibleBackground extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      color: Colors.red,
      child: Padding(
        padding: const EdgeInsets.only(left: 20, right: 20),
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.center,
          mainAxisAlignment: MainAxisAlignment.spaceBetween,
          children: [
            Icon(
              Icons.delete_forever,
              size: 35,
              color: Colors.white,
            ),
            Icon(
              Icons.delete_forever,
              size: 35,
              color: Colors.white,
            ),
          ],
        ),
      ),
    );
  }
}
