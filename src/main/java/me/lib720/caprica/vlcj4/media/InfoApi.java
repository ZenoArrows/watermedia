/*
 * This file is part of VLCJ.
 *
 * VLCJ is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * VLCJ is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with VLCJ.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009-2019 Caprica Software Limited.
 */

package me.lib720.caprica.vlcj4.media;

import me.lib720.caprica.vlcj4.binding.support.strings.NativeString;
import me.lib720.caprica.vlcj4.binding.internal.libvlc_media_stats_t;
import me.lib720.caprica.vlcj4.player.base.State;

import java.util.List;

import static me.lib720.caprica.vlcj4.binding.lib.LibVlc.libvlc_media_get_duration;
import static me.lib720.caprica.vlcj4.binding.lib.LibVlc.libvlc_media_get_mrl;
import static me.lib720.caprica.vlcj4.binding.lib.LibVlc.libvlc_media_get_state;
import static me.lib720.caprica.vlcj4.binding.lib.LibVlc.libvlc_media_get_stats;
import static me.lib720.caprica.vlcj4.binding.lib.LibVlc.libvlc_media_get_type;

/**
 * Behaviour pertaining to media information, providing things like the media resource locator, type, state and duration
 * of this media. Also provides access to the various media tracks and playback statistics.
 */
public final class InfoApi extends BaseApi {

    private final libvlc_media_stats_t statsInstance = new libvlc_media_stats_t();

    InfoApi(Media media) {
        super(media);
    }

    /**
     * Get the media resource locator for the current media.
     *
     * @return media resource locator
     */
    public String mrl() {
        return NativeString.copyAndFreeNativeString(libvlc_media_get_mrl(mediaInstance));
    }

    /**
     * Get the current media type
     *
     * @return media type
     */
    public MediaType type() {
        return MediaType.mediaType(libvlc_media_get_type(mediaInstance));
    }

    /**
     * Get the media state
     *
     * @return state
     */
    public State state() {
        return State.state(libvlc_media_get_state(mediaInstance));
    }

    /**
     * Get the duration
     *
     * @return duration, milliseconds
     */
    public long duration() {
        return libvlc_media_get_duration(mediaInstance);
    }

    /**
     * Get the list of all media tracks, or only those that match specified types.
     *
     * @param types zero or more track types, if none specified then all track types will be returned
     * @return track information, empty list if no tracks
     */
    public List<? extends TrackInfo> tracks(TrackType... types) {
        return TrackInformation.getTrackInfo(mediaInstance, types);
    }

    /**
     * Get the list of audio tracks on the current media.
     *
     * @return audio tracks, empty list if none
     */
    @SuppressWarnings("unchecked")
    public List<AudioTrackInfo> audioTracks() {
        return (List<AudioTrackInfo>) tracks(TrackType.AUDIO);
    }

    /**
     * Get the list of video tracks on the current media.
     *
     * @return video tracks, empty list if none
     */
    @SuppressWarnings("unchecked")
    public List<VideoTrackInfo> videoTracks() {
        return (List<VideoTrackInfo>) tracks(TrackType.VIDEO);
    }

    /**
     * Get the list of text (subtitle) tracks on the current media.
     *
     * @return text tracks, empty list if none
     */
    @SuppressWarnings("unchecked")
    public List<TextTrackInfo> textTracks() {
        return (List<TextTrackInfo>) tracks(TrackType.TEXT);
    }

    /**
     * Get playback statistics for the current media.
     *
     * @param mediaStatistics caller-supplied structure to receive the media statistics
     * @return <code>true</code> if the statistics were updated; <code>false</code> on error
     */
    public boolean statistics(MediaStatistics mediaStatistics) {
        if (libvlc_media_get_stats(mediaInstance, statsInstance) != 0) {
            mediaStatistics.apply(statsInstance);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get playback statistics for the current media.
     *
     * @return media statistics, or <code>null</code> on error
     */
    public MediaStatistics statistics() {
        if (libvlc_media_get_stats(mediaInstance, statsInstance) != 0) {
            MediaStatistics mediaStatistics = new MediaStatistics();
            mediaStatistics.apply(statsInstance);
            return mediaStatistics;
        } else {
            return null;
        }
    }

}
