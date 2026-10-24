Add-Type -AssemblyName System.Drawing

$outputDir = ".\src\main\resources\assets\capturesphere\textures\item"

$grid = @(
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
)

$gridCaptured = @(
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
)

function Create-SpherePng($filename, $palette, $useCaptured) {
    $bmp = New-Object System.Drawing.Bitmap(16, 16)
    $lines = if ($useCaptured) { $gridCaptured } else { $grid }

    for ($y = 0; $y -lt 16; $y++) {
        $line = $lines[$y]
        $x = 0
        $idx = 0
        while ($idx -lt $line.Length -and $x -lt 16) {
            $char2 = if ($idx + 2 -le $line.Length) { $line.Substring($idx, 2) } else { "" }
            if ($palette.ContainsKey($char2)) {
                $color = $palette[$char2]
                $bmp.SetPixel($x, $y, $color)
                $idx += 2
                $x++
            } else {
                $char1 = $line.Substring($idx, 1)
                $color = $palette[$char1]
                $bmp.SetPixel($x, $y, $color)
                $idx += 1
                $x++
            }
        }
    }

    $fullPath = Join-Path $outputDir $filename
    $fs = [System.IO.FileStream]::new($fullPath, [System.IO.FileMode]::Create)
    $bmp.Save($fs, [System.Drawing.Imaging.ImageFormat]::Png)
    $fs.Close()
    $fs.Dispose()
    $bmp.Dispose()
    Write-Output "Saved: $fullPath"
}

# Copper
$copperPalette = @{
    "."  = [System.Drawing.Color]::FromArgb(0, 0, 0, 0)
    "B"  = [System.Drawing.Color]::FromArgb(255, 0x38, 0x1E, 0x14)
    "D"  = [System.Drawing.Color]::FromArgb(255, 0x78, 0x38, 0x24)
    "M"  = [System.Drawing.Color]::FromArgb(255, 0xA8, 0x4E, 0x30)
    "C"  = [System.Drawing.Color]::FromArgb(255, 0xD0, 0x68, 0x38)
    "L"  = [System.Drawing.Color]::FromArgb(255, 0xF0, 0x8C, 0x54)
    "H"  = [System.Drawing.Color]::FromArgb(255, 0xFF, 0xBA, 0x88)
    "X"  = [System.Drawing.Color]::FromArgb(255, 0x24, 0x14, 0x0E)
    "P1" = [System.Drawing.Color]::FromArgb(255, 0x50, 0x18, 0x64)
    "P2" = [System.Drawing.Color]::FromArgb(255, 0x8C, 0x30, 0xB0)
    "P3" = [System.Drawing.Color]::FromArgb(255, 0xBE, 0x60, 0xE4)
    "P4" = [System.Drawing.Color]::FromArgb(255, 0xF0, 0xC4, 0xFF)
}

# Iron
$ironPalette = @{
    "."  = [System.Drawing.Color]::FromArgb(0, 0, 0, 0)
    "B"  = [System.Drawing.Color]::FromArgb(255, 0x24, 0x28, 0x2C)
    "D"  = [System.Drawing.Color]::FromArgb(255, 0x50, 0x56, 0x5E)
    "M"  = [System.Drawing.Color]::FromArgb(255, 0x76, 0x7E, 0x88)
    "C"  = [System.Drawing.Color]::FromArgb(255, 0x9E, 0xA8, 0xB4)
    "L"  = [System.Drawing.Color]::FromArgb(255, 0xC8, 0xD2, 0xDC)
    "H"  = [System.Drawing.Color]::FromArgb(255, 0xF4, 0xF8, 0xFC)
    "X"  = [System.Drawing.Color]::FromArgb(255, 0x18, 0x1C, 0x20)
    "P1" = [System.Drawing.Color]::FromArgb(255, 0x06, 0x28, 0x24)
    "P2" = [System.Drawing.Color]::FromArgb(255, 0x0E, 0x54, 0x48)
    "P3" = [System.Drawing.Color]::FromArgb(255, 0x20, 0xA4, 0x88)
    "P4" = [System.Drawing.Color]::FromArgb(255, 0x6C, 0xF4, 0xD4)
}

# Gold
$goldPalette = @{
    "."  = [System.Drawing.Color]::FromArgb(0, 0, 0, 0)
    "B"  = [System.Drawing.Color]::FromArgb(255, 0x4C, 0x36, 0x08)
    "D"  = [System.Drawing.Color]::FromArgb(255, 0x8C, 0x64, 0x10)
    "M"  = [System.Drawing.Color]::FromArgb(255, 0xC0, 0x8E, 0x1C)
    "C"  = [System.Drawing.Color]::FromArgb(255, 0xEC, 0xB8, 0x28)
    "L"  = [System.Drawing.Color]::FromArgb(255, 0xFC, 0xD8, 0x54)
    "H"  = [System.Drawing.Color]::FromArgb(255, 0xFF, 0xF4, 0xA0)
    "X"  = [System.Drawing.Color]::FromArgb(255, 0x34, 0x22, 0x04)
    "P1" = [System.Drawing.Color]::FromArgb(255, 0x78, 0x90, 0xA0)
    "P2" = [System.Drawing.Color]::FromArgb(255, 0xB0, 0xC8, 0xD8)
    "P3" = [System.Drawing.Color]::FromArgb(255, 0xE0, 0xEE, 0xF8)
    "P4" = [System.Drawing.Color]::FromArgb(255, 0xFF, 0xFF, 0xFF)
}

Create-SpherePng "copper_sphere.png" $copperPalette $false
Create-SpherePng "copper_sphere_captured.png" $copperPalette $true
Create-SpherePng "iron_sphere.png" $ironPalette $false
Create-SpherePng "iron_sphere_captured.png" $ironPalette $true
Create-SpherePng "gold_sphere.png" $goldPalette $false
Create-SpherePng "gold_sphere_captured.png" $goldPalette $true
