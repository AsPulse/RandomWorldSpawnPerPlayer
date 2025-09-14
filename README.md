# RandomWorldSpawnPerPlayer

プレイヤーごとにランダムなワールドスポーンを設定するSpigotプラグイン

## Config

デフォルト
```yaml
center:
  x: 0
  z: 0
radius: 1000

spawnable-condition:
  n-above-air-blocks: 2
  n-below-opaque-blocks: 1

worldspawn-condition:
  n-above-air-blocks: 10
  n-below-opaque-blocks: 5
  y-upper-limit: 100
  y-lower-limit: 45
```

### center
プレイヤーごとのワールドスポーンが分布する範囲の、中心座標を指定します。

- `x`: 中心のX座標（デフォルト: 0）
- `z`: 中心のZ座標（デフォルト: 0）

### radius
プレイヤーごとのワールドスポーンが分布する範囲の、半径（ブロック単位）を指定します。  
デフォルト: 1000

### spawnable-condition
プレイヤーがスポーン可能な地点の条件を指定します。

- `n-above-air-blocks`: スポーン地点の上に必要な空気ブロックの数（デフォルト: 2）
- `n-below-opaque-blocks`: スポーン地点の下に必要な不透明ブロックの数（デフォルト: 1）

### worldspawn-condition
プレイヤーごとのワールドスポーン地点になり得る条件を指定します。

- `n-above-air-blocks`: スポーン地点の上に必要な空気ブロックの数（デフォルト: 10）
- `n-below-opaque-blocks`: スポーン地点の下に必要な不透明ブロックの数（デフォルト: 5）
- `y-upper-limit`: Y座標の上限（デフォルト: 100）
- `y-lower-limit`: Y座標の下限（デフォルト: 45）
