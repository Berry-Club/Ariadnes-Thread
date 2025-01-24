# 1.21

### 1.2.0

- Updated to 1.21
- Removed the config for limiting based on NBT size
- Removed the config for showing NBT in tooltip
- Changed the config for max location size to be unlimited by default

### 1.2.1

- Update NeoForge to 21.0.52-beta from 21.0.14-beta
- Removed LocationItemComponent, HistoryItemComponent now has a List<BlockPos> instead. As a consequence, it no longer stores the exact float location. This is better for performance.
- Changed the text in the tooltip

### 1.2.2

- Updated NeoForge to 21.0.75-beta
- Fixed the recipe still using the tag #forge:nuggets/gold instead of #c:nuggets/gold

### 1.3.0

- Improved the rendering, so it's more than a single pixel wide
- Fixed the item checking if you're crouching rather than if `isSecondaryUseActive`, so now you can toggle while flying
- Added config screens
- Line thickness is in the startup config

### 1.4.0

- The thread now trims any loops in the history. If you get close to any point in the history, it'll remove any points after it.