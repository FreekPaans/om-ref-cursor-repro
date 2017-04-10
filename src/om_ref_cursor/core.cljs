(ns om-ref-cursor.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)

(defonce app-state (atom {:1 {:text "this is no-root-1"}
                          :2 {:text "this is no-root-2"}}))
(om/root
  (fn [data owner]
    (reify
      om/IRender
      (render [_]
        (let [text (om/observe owner
                     (om/ref-cursor (:1 (om/root-cursor app-state))))]
          (dom/div nil (:text text)
                   (dom/button
                     #js
                     {:onClick (fn []
                                 (swap! app-state assoc-in [:1 :text] "clicked")
                                 (swap! app-state assoc-in [:2 :text] "clicked"))}
                     "Cliking me should update no-root-1 and no-root-2"))))))
  {}
  {:target (. js/document (getElementById "no-root-1"))})

(om/root
  (fn [data owner]
    (reify
      om/IRender
      (render [_]
        (let [text (om/observe owner
                               (om/ref-cursor (:2 (om/root-cursor app-state))))]
          (dom/div nil (:text text))))))
  {}
  {:target (. js/document (getElementById "no-root-2"))})

(om/root
  (fn [data owner]
    (reify
      om/IRender
      (render [_]
        (dom/div nil "uncomment me"))))
  app-state
  {:target (. js/document (getElementById "no-root-3"))})
