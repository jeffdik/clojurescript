;   Copyright (c) Rich Hickey. All rights reserved.
;   The use and distribution terms for this software are covered by the
;   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
;   which can be found in the file epl-v10.html at the root of this distribution.
;   By using this software in any fashion, you are agreeing to be bound by
;   the terms of this license.
;   You must not remove this notice, or any other, from this software.

(ns twitterbuzz.timeline
  (:require [twitterbuzz.core :as buzz]
            [twitterbuzz.dom-helpers :as dom]))

(defn timeline-element [tweet]
  (dom/build [:div {:class "tweet"}
              [:img {:src (:profile_image_url tweet) :class "profile-pic"}]
              [:div {:class "user-name"} (:from_user tweet)]
              [:div {:class "tweet-text"} (dom/html (:text tweet))]]))

(defn update-timeline [tweets]
  (do (buzz/set-tweet-status :okay (str (:tweet-count @buzz/state) " tweets"))
      (doseq [tweet (reverse tweets)]
        (dom/insert-at (dom/get-element :timeline-content)
                       (timeline-element tweet)
                       0))))

(buzz/register :track-clicked #(dom/remove-children :timeline-content))
(buzz/register :new-tweets update-timeline)
