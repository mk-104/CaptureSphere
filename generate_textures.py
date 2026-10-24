"""
Capture Sphere Mod - 16x16 アイテムテクスチャ生成スクリプト
Pillow (PIL) を使用して 16x16 ピクセルの球体テクスチャPNGを生成します。

インストール:
    pip install pillow

実行:
    python generate_textures.py
"""

import os
from PIL import Image

# 16x16 ピクセルのグリッド定義
# 2文字キー（P1, P2, P3, P4）と1文字キーの混成
GRID = [
    "................",
    ".....BBBBBB.....",
    "...BBLHHCCCDDB..",
    "..BLHHCCCCCCDDB.",
    "..BLHCCCCCCCMDD.",
    ".BLCCCCCCCCCCDB.",
    ".BXXXXXXXXXXXXB.",
    ".BXXP1P2P2P1XXB.",
    ".BXXP2P4P3P2XXB.",
    ".BXXP1P2P2P1XXB.",
    ".BXXXXXXXXXXXXB.",
    ".BMCCCCCCCCCMDDB",
    "..BMMCCCCCCMDD..",
    "...BDDMMMMDDB...",
    ".....BBBBBB.....",
    "................"
]

# モブ格納時（コア活性化）のグリッド
GRID_CAPTURED = [
    "................",
    ".....BBBBBB.....",
    "...BBLHHCCCDDB..",
    "..BLHHCCCCCCDDB.",
    "..BLHCCCCCCCMDD.",
    ".BLCCCCCCCCCCDB.",
    ".BXXXXXXXXXXXXB.",
    ".BXXP3P4P4P3XXB.",
    ".BXXP4P4P4P4XXB.",
    ".BXXP3P4P4P3XXB.",
    ".BXXXXXXXXXXXXB.",
    ".BMCCCCCCCCCMDDB",
    "..BMMCCCCCCMDD..",
    "...BDDMMMMDDB...",
    ".....BBBBBB.....",
    "................"
]

PALETTES = {
    "copper_sphere": {
        "palette": {
            ".": (0, 0, 0, 0),
            "B": (0x38, 0x1E, 0x14, 255),
            "D": (0x78, 0x38, 0x24, 255),
            "M": (0xA8, 0x4E, 0x30, 255),
            "C": (0xD0, 0x68, 0x38, 255),
            "L": (0xF0, 0x8C, 0x54, 255),
            "H": (0xFF, 0xBA, 0x88, 255),
            "X": (0x24, 0x14, 0x0E, 255),
            "P1": (0x50, 0x18, 0x64, 255),  # アメジスト深紫
            "P2": (0x8C, 0x30, 0xB0, 255),  # アメジスト紫
            "P3": (0xBE, 0x60, 0xE4, 255),  # アメジスト明紫
            "P4": (0xF0, 0xC4, 0xFF, 255)   # アメジスト発光
        }
    },
    "iron_sphere": {
        "palette": {
            ".": (0, 0, 0, 0),
            "B": (0x24, 0x28, 0x2C, 255),
            "D": (0x50, 0x56, 0x5E, 255),
            "M": (0x76, 0x7E, 0x88, 255),
            "C": (0x9E, 0xA8, 0xB4, 255),
            "L": (0xC8, 0xD2, 0xDC, 255),
            "H": (0xF4, 0xF8, 0xFC, 255),
            "X": (0x18, 0x1C, 0x20, 255),
            "P1": (0x06, 0x28, 0x24, 255),  # エンダー深緑
            "P2": (0x0E, 0x54, 0x48, 255),  # エンダーティール
            "P3": (0x20, 0xA4, 0x88, 255),  # エンダー明緑
            "P4": (0x6C, 0xF4, 0xD4, 255)   # エンダー発光
        }
    },
    "gold_sphere": {
        "palette": {
            ".": (0, 0, 0, 0),
            "B": (0x4C, 0x36, 0x08, 255),
            "D": (0x8C, 0x64, 0x10, 255),
            "M": (0xC0, 0x8E, 0x1C, 255),
            "C": (0xEC, 0xB8, 0x28, 255),
            "L": (0xFC, 0xD8, 0x54, 255),
            "H": (0xFF, 0xF4, 0xA0, 255),
            "X": (0x34, 0x22, 0x04, 255),
            "P1": (0x78, 0x90, 0xA0, 255),  # ガストの涙・影
            "P2": (0xB0, 0xC8, 0xD8, 255),  # ガストの涙・淡水色
            "P3": (0xE0, 0xEE, 0xF8, 255),  # ガストの涙・白水色
            "P4": (0xFF, 0xFF, 0xFF, 255)   # ガストの涙・光沢
        }
    }
}

def render_image(grid_lines, palette):
    img = Image.new("RGBA", (16, 16), (0, 0, 0, 0))
    for y, line in enumerate(grid_lines):
        x = 0
        idx = 0
        while idx < len(line) and x < 16:
            char2 = line[idx:idx+2]
            if char2 in palette:
                img.putpixel((x, y), palette[char2])
                idx += 2
                x += 1
            else:
                char1 = line[idx:idx+1]
                img.putpixel((x, y), palette.get(char1, (0, 0, 0, 0)))
                idx += 1
                x += 1
    return img

def main():
    output_dir = os.path.join(
        "src", "main", "resources", "assets", "capturesphere", "textures", "item"
    )
    os.makedirs(output_dir, exist_ok=True)

    for name, config in PALETTES.items():
        palette = config["palette"]
        
        # 通常テクスチャ
        img_normal = render_image(GRID, palette)
        path_normal = os.path.join(output_dir, f"{name}.png")
        img_normal.save(path_normal)
        print(f"Generated: {path_normal}")

        # モブ格納時テクスチャ
        img_captured = render_image(GRID_CAPTURED, palette)
        path_captured = os.path.join(output_dir, f"{name}_captured.png")
        img_captured.save(path_captured)
        print(f"Generated: {path_captured}")

if __name__ == "__main__":
    main()
