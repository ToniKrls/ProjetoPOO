import os
import sys

sys.path.insert(0, os.path.join(os.path.dirname(__file__)))

from sisu_analyser.MainController import Main

if __name__ == "__main__":
    app = Main()
    app.mainloop()