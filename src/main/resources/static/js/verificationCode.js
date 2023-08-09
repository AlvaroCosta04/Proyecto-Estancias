document.addEventListener('DOMContentLoaded', function() {
  var box1 = document.getElementById('box1');
  var box2 = document.getElementById('box2');
  var box3 = document.getElementById('box3');
  var box4 = document.getElementById('box4');
  var box5 = document.getElementById('box5');
  var box6 = document.getElementById('box6');
  var box7 = document.getElementById('box7');

  var boxes = [box1, box2, box3, box4, box5, box6, box7];

  var verificationCode = '';

  boxes.forEach(function(box, index) {
    box.addEventListener('input', function() {
      verificationCode = '';
      boxes.forEach(function(box) {
        verificationCode += box.value;
      });
      document.getElementById('verificationCode').value = verificationCode;

      if (box.value.length === 1) {
        if (index < boxes.length - 1) {
          boxes[index + 1].focus();
        }
      }
    });

    box.addEventListener('keydown', function(event) {
      if (event.key === 'Backspace' && box.value.length === 0) {
        if (index > 0) {
          boxes[index - 1].focus();
        }
      }
    });
  });
});


