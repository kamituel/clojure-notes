(ns notes.chapter03gameoflife
	(:use clojure.pprint))

(defn -main
	[]

	(defn empty-board
		[w h]
		(vec (repeat w (vec (repeat h nil)))))

	(defn populate
		[board living-cells]
		(reduce (fn [board coordinates]
				(assoc-in board coordinates :on)) 
			board
			living-cells)
		)

	(defn neighbours
		[[x y]]
		(for [dx [-1 0 1] dy [-1 0 1] :when (not= 0 dx dy)]
			[(+ x dx) (+ y dy)]))

	(defn count-neighbours
		[board loc]
		(count
			(filter
				#(get-in board %)
				(neighbours loc))))

	(defn indexed-step
		[board]
		(let [w (count board)
			h (count (first board))]
			(loop [new-board board x 0 y 0]
				(cond
					(>= x w) new-board
					(>= y h) (recur new-board (inc x) 0)
					:else
						(let [new-liveliness
							(case (count-neighbours board [x y])
								2 (get-in board [x y])
								3 :on
								nil)]
							(recur (assoc-in new-board [x y] new-liveliness) x (inc y)))))))

	; This one avoids loop in favor of two reduce's.
	(defn indexed-step2
		[board]
		(let [w (count board)
			h (count (first board))]
			(reduce (fn [new-board x]
				(reduce (fn [new-board y]
					(let [new-liveliness
						(case (count-neighbours board [x y])
							2 (get-in board [x y])
							3 :on
							nil)]
						(assoc-in new-board [x y] new-liveliness)))
				new-board (range h)))
			board (range w))))

	; This one collapses nested reduce's.
	(defn indexed-step3
		[board]
		(let [w (count board)
			h (count (first board))]
			(reduce (fn [new-board [x y]]
				(let [new-liveliness
					(case (count-neighbours board [x y])
						2 (get-in board [x y])
						3 :on
						nil)]
					(assoc-in new-board [x y] new-liveliness)))
			board (for [x (range w) y (range h)] [x y]))))

	(defn window
		[coll]
		(partition 3 1 (concat [nil] coll [nil])))

	(defn cell-block
		[[left mid right]]
		(window (map vector (or left (repeat nil)) mid (or right (repeat nil)))))

	(defn liveliness
		[block]
		(let [[_ [_ center _] _] block]
			(case (- (count (filter #{:on} (apply concat block)))
					(if (= :on center) 1 0))
			2 center
			3 :on
			nil)))

	(defn- step-row
		[rows-triple]
		(vec (map liveliness (cell-block rows-triple))))

	(defn index-free-step
		[board]
		(vec (map step-row (window board))))

	; (pprint (cell-block (window [1 2 3])))

	(def board (populate (empty-board 6 6) #{[2 0] [2 1] [2 2] [1 2] [0 1]}))
	(pprint board)
	;(-> (iterate index-free-step board) (nth 8) pprint)

	"Next-level approach"

	(defn step
		[living-cells]
		(set (for [[location living-count] (frequencies (mapcat neighbours living-cells))
			:when (or 
				(= living-count 3) 
				(and (= living-count 2) (living-cells location)))]
			location)))

	(pprint (populate (empty-board 6 6) (step #{[2 0] [2 1] [2 2] [1 2] [0 1]})))

	"maze"

	(defn maze
		[walls]
		(let [paths (reduce (fn [index [a b]]
								(merge-with into index {a [b] b [a]}))
							{} (map seq walls))
			start-loc (rand-nth (keys paths))]
		(loop [walls walls
			unvisited (disj (set (keys paths)) start-loc)]
			(if-let [loc (when-let [s (seq unvisited)] (rand-nth s))]
				(let [walk (iterate (comp rand-nth paths) loc)
					steps (zipmap (take-while unvisited walk) (next walk))]
					(recur (reduce disj walls (map set steps))
						(reduce disj unvisited (keys steps))))
				walls))))

	(defn grid
		[w h]
		(set (concat
			(for [i (range (dec w)) j (range h)] #{[i j] [(inc i) j]})
			(for [i (range w) j (range (dec h))] #{[i j] [i (inc j)]}))))

	(defn draw
		[w h maze]
		(doto (javax.swing.JFrame. "Maze")
			(.setContentPane
				(doto (proxy [javax.swing.JPanel] []
					(paintComponent [^java.awt.Graphics g]
						(let [g (doto ^java.awt.Graphics2D (.create g)
							(.scale 10 10)
							(.translate 1.5 1.5)
							(.setStroke (java.awt.BasicStroke. 0.4)))]
						(.drawRect g -1 -1 w h)
						(doseq [[[xa ya] [xb yb]] (map sort maze)]
							(let [[xc yc] (if (= xa xb)
								[(dec xa) ya]
								[xa (dec ya)])]
							(.drawLine g xa ya xc yc))))))
				(.setPreferredSize (java.awt.Dimension. (* 10 (inc w)) (* 10 (inc h))))))
			.pack
			(.setVisible true)))

	(pprint (grid 2 2))

	(let [w 2]
		(draw w w (maze (grid w w))))

	)